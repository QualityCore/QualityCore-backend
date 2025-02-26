package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.LineMaterialDTO;
import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.entity.*;
import com.org.qualitycore.work.model.repository.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Schema(description = "작업지시서 관련 Service")
public class WorkService {

    private final WorkRepository workRepository;
    private final JPAQueryFactory queryFactory;
    private final WorkEmployeeRepository employeeRepository;
    private final WorkPlanLineRepository planLineRepository;
    private final WorkPlanProductRepository planProductRepository;
    private final ProcessTrackingRepository processTrackingRepository;
    private final LineMaterialRepository lineMaterialRepository;
    private final ModelMapper modelMapper;

    // 작업지시서 전체 조회
    public List<WorkFindAllDTO> findAllWorkOrders() {
        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

        // 작업지시서 기본 정보 조회
        List<WorkFindAllDTO> workOrders = queryFactory
                .select(Projections.fields(WorkFindAllDTO.class,
                        wo.lotNo.as("lotNo"),
                        wo.workProgress.as("workProgress"),
                        wo.workEtc.as("workEtc"),
                        pp.productName.as("productName"),
                        pl.lineNo.as("lineNo"),
                        pl.planQty.as("planQty"),
                        pl.startDate.as("startDate"),
                        pl.endDate.as("endDate"),
                        pt.processStatus.as("processStatus"),
                        pt.processName.as("processName"),
                        pt.trackingId.as("trackingId"),
                        e.workTeam.as("workTeam")
                ))
                .from(wo)
                .join(pl).on(wo.planLine.planLineId.eq(pl.planLineId))
                .join(pp).on(pl.planProductId.eq(pp.planProductId))
                .join(e).on(wo.employee.empId.eq(e.empId))
                .join(pt).on(wo.lotNo.eq(pt.workOrders.lotNo))  // 수정된 부분
                .fetch();

        // 각 작업지시서에 자재 정보를 추가
        for (WorkFindAllDTO workOrder : workOrders) {
            // 자재 정보 조회 및 추가
            List<LineMaterialDTO> lineMaterials = queryFactory
                    .select(Projections.fields(LineMaterialDTO.class,
                            lm.lineMaterialId.as("lineMaterialId"),
                            lm.materialName.as("materialName"),
                            lm.materialType.as("materialType"),
                            lm.unit.as("unit"),
                            lm.requiredQtyPerUnit.as("requiredQtyPerUnit"),
                            lm.pricePerUnit.as("pricePerUnit"),
                            lm.totalCost.as("totalCost")
                    ))
                    .from(lm)
                    .where(lm.workOrders.lotNo.eq(workOrder.getLotNo()))
                    .fetch();

            workOrder.setLineMaterials(lineMaterials);
        }

        return workOrders;
    }

    // 특정 작업지시서 조회
    public WorkFindAllDTO findByCodeWorkOrder(String lotNo) {
        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

        // 특정 작업지시서 기본 정보 조회
        WorkFindAllDTO workOrder = queryFactory
                .select(Projections.fields(WorkFindAllDTO.class,
                        wo.lotNo.as("lotNo"),
                        wo.workProgress.as("workProgress"),
                        wo.workEtc.as("workEtc"),
                        pp.productName.as("productName"),
                        pl.lineNo.as("lineNo"),
                        pl.planQty.as("planQty"),
                        pl.startDate.as("startDate"),
                        pl.endDate.as("endDate"),
                        pt.processStatus.as("processStatus"),
                        pt.trackingId.as("trackingId"),
                        pt.processName.as("processName"),
                        e.workTeam.as("workTeam")
                ))
                .from(wo)
                .join(pl).on(wo.planLine.planLineId.eq(pl.planLineId))
                .join(pp).on(pl.planProductId.eq(pp.planProductId))
                .join(e).on(wo.employee.empId.eq(e.empId))
                .join(pt).on(wo.lotNo.eq(pt.workOrders.lotNo))
                .where(wo.lotNo.eq(lotNo))
                .fetchOne();

        if (workOrder == null) {
            throw new IllegalArgumentException("작업지시서를 찾을 수 없습니다: " + lotNo);
        }

        // 자재 정보 조회 및 추가
        List<LineMaterialDTO> lineMaterials = queryFactory
                .select(Projections.fields(LineMaterialDTO.class,
                        lm.lineMaterialId.as("lineMaterialId"),
                        lm.materialName.as("materialName"),
                        lm.materialType.as("materialType"),
                        lm.unit.as("unit"),
                        lm.requiredQtyPerUnit.as("requiredQtyPerUnit"),
                        lm.pricePerUnit.as("pricePerUnit"),
                        lm.totalCost.as("totalCost")
                ))
                .from(lm)
                .where(lm.workOrders.lotNo.eq(lotNo))
                .fetch();

        workOrder.setLineMaterials(lineMaterials);

        return workOrder;
    }

    // 작업지시서 등록
    @Transactional
    public void createWorkOrder(WorkFindAllDTO work) {
        // 1️⃣ 사원 정보 조회
        Employee employee = employeeRepository.findById(work.getEmpId())
                .orElseThrow(() -> new IllegalArgumentException("사원을 찾을 수 없습니다: " + work.getEmpId()));

        // 2️⃣ 제품, 생산 라인, 진행 상태 조회
        PlanLine planLine = planLineRepository.findById(work.getPlanLineId())
                .orElseThrow(() -> new IllegalArgumentException("생산라인을 찾을 수 없습니다: " + work.getPlanLineId()));
        PlanProduct planProduct = planProductRepository.findById(work.getPlanProductId())
                .orElseThrow(() -> new IllegalArgumentException("생산제품을 찾을 수 없습니다: " + work.getPlanProductId()));
        processTracking processTracking = processTrackingRepository.findById(work.getTrackingId())
                .orElseThrow(() -> new IllegalArgumentException("진행상태를 찾을 수 없습니다: " + work.getTrackingId()));

        // 3️⃣ 작업지시서 객체 생성 및 설정
        WorkOrders workOrder = modelMapper.map(work, WorkOrders.class);
        workOrder.setPlanProduct(planProduct);
        workOrder.setEmployee(employee);
        workOrder.setPlanLine(planLine);
        workOrder.setProcessTracking(processTracking);

        // 4️⃣ 가장 최신 작업지시서 LOT 번호 조회 및 새 LOT 번호 생성
        String maxWorkOrderId = workRepository.findTopByOrderByLotNoDesc()
                .map(WorkOrders::getLotNo)
                .orElse(null);
        String newWorkOrderId = generateNewWorkOrderId(maxWorkOrderId);
        workOrder.setLotNo(newWorkOrderId);  // 새 LOT 번호 설정

        // 5️⃣ 배정된 생산 수량 가져오기 (double로 형변환)
        double assignedQty = (double) planLine.getPlanQty();  // 생산 라인에서 배정된 수량을 double로 변환

        // 6️⃣ 자재 목록 생성 및 계산
        List<LineMaterial> lineMaterials = work.getLineMaterials().stream()
                .map(lineMaterialDTO -> {
                    LineMaterial lineMaterial = modelMapper.map(lineMaterialDTO, LineMaterial.class);

                    // 7️⃣ 자재 사용량 및 비용 계산 (BigDecimal로 계산)
                    BigDecimal requiredQtyPerUnit = BigDecimal.valueOf(lineMaterialDTO.getRequiredQtyPerUnit());
                    BigDecimal pricePerUnit = BigDecimal.valueOf(lineMaterialDTO.getPricePerUnit());
                    BigDecimal totalRequiredQty = requiredQtyPerUnit.multiply(BigDecimal.valueOf(assignedQty));  // 필요량 * 배정수량
                    BigDecimal totalCost = totalRequiredQty.multiply(pricePerUnit);  // 총 비용 계산

                    lineMaterial.setTotalCost(totalCost);  // 총 비용 설정
                    lineMaterial.setWorkOrders(workOrder);  // WorkOrders 설정
                    return lineMaterial;
                })
                .collect(Collectors.toList());

        // 8️⃣ 작업지시서에 자재 목록 설정
        workOrder.setLineMaterial(lineMaterials);

        // 9️⃣ 작업지시서 저장
        workRepository.save(workOrder);
    }

    // auto increment 방식으로 작업지시서 번호 생성
    private String generateNewWorkOrderId(String maxWorkOrderId) {
        // 현재 날짜를 YYYYMMDD 형식으로 추출
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if (maxWorkOrderId == null) {
            // 첫 번째 작업지시서 번호는 "LOT" + 오늘 날짜 + "01"
            return "LOT" + currentDate + "01";
        }

        // 기존 작업지시서 번호에서 날짜 부분과 순번을 추출
        String existingDate = maxWorkOrderId.substring(3, 11);  // "LOT20250225"에서 날짜 부분만 추출
        String existingSequence = maxWorkOrderId.substring(11);  // 순번 부분 ("01", "02" 등)

        if (existingDate.equals(currentDate)) {
            // 같은 날짜의 경우 순번을 증가
            int newSequence = Integer.parseInt(existingSequence) + 1;
            return "LOT" + currentDate + String.format("%02d", newSequence);  // 두 자리 숫자로 포맷
        } else {
            // 날짜가 다르면 첫 번째 작업지시서로 설정
            return "LOT" + currentDate + "01";
        }
    }

    // 작업지시서 삭제
    @Transactional
    public void workOrderDelete(String lotNo) {

        workRepository.deleteById(lotNo);

        modelMapper.map(lotNo, WorkFindAllDTO.class);
    }
}
