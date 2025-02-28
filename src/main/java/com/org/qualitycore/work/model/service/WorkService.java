package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.*;
import com.org.qualitycore.work.model.entity.*;
import com.org.qualitycore.work.model.repository.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final WorkPlanMstRepository workPlanMstRepository;
    private final ModelMapper modelMapper;

    // 작업지시서 전체 조회
    public Page<WorkFindAllDTO> findAllWorkOrders(Pageable pageable) {
        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

        // 전체 데이터 개수 조회
        long totalCount = Optional.ofNullable(
                queryFactory
                        .select(wo.count())
                        .from(wo)
                        .join(pl).on(wo.planLine.planLineId.eq(pl.planLineId))
                        .join(pp).on(pl.planProductId.eq(pp.planProductId))
                        .join(e).on(wo.employee.empId.eq(e.empId))
                        .join(pt).on(wo.lotNo.eq(pt.workOrders.lotNo))
                        .fetchOne()
        ).orElse(0L);

        // 페이지네이션 적용하여 데이터 조회
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
                .join(pt).on(wo.lotNo.eq(pt.workOrders.lotNo))
                .orderBy(wo.lotNo.desc())
                .offset(pageable.getOffset()) // 시작 위치 지정
                .limit(pageable.getPageSize()) // 페이지 크기 지정
                .fetch();

        // 각 작업지시서에 자재 정보 추가
        for (WorkFindAllDTO workOrder : workOrders) {
            List<LineMaterialDTO> lineMaterials = queryFactory
                    .select(Projections.fields(LineMaterialDTO.class,
                            lm.lineMaterialId.as("lineMaterialId"),
                            lm.materialName.as("materialName"),
                            lm.totalQty.castToNum(Integer.class).as("totalQty"),
                            lm.unit.as("unit"),
                            lm.requiredQtyPerUnit.castToNum(Double.class).as("requiredQtyPerUnit"),
                            lm.processStep.as("processStep")
                    ))
                    .from(lm)
                    .where(lm.workOrders.lotNo.eq(workOrder.getLotNo()))
                    .fetch();

            workOrder.setLineMaterials(lineMaterials);
        }

        // Page 객체로 변환하여 반환
        return new PageImpl<>(workOrders, pageable, totalCount);
    }


    public Page<WorkFindAllDTO> findAllSearchWorkOrders(String lotNo, Pageable pageable) {
        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

        // 쿼리 빌더 생성
        JPAQuery<WorkFindAllDTO> query = queryFactory
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
                .join(pt).on(wo.lotNo.eq(pt.workOrders.lotNo));

        // ✅ 검색 조건 추가 (LotNo가 있을 경우 필터링)
        if (lotNo != null && !lotNo.trim().isEmpty()) {
            query.where(wo.lotNo.containsIgnoreCase(lotNo));
        }

        // 전체 데이터 개수 조회
        long totalCount = Optional.ofNullable(
                queryFactory.select(wo.count())
                        .from(wo)
                        .where(lotNo != null && !lotNo.trim().isEmpty() ? wo.lotNo.containsIgnoreCase(lotNo) : null)
                        .fetchOne()
        ).orElse(0L);

        // 페이지네이션 적용하여 데이터 조회
        List<WorkFindAllDTO> workOrders = query
                .orderBy(wo.lotNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 각 작업지시서에 자재 정보 추가
        for (WorkFindAllDTO workOrder : workOrders) {
            List<LineMaterialDTO> lineMaterials = queryFactory
                    .select(Projections.fields(LineMaterialDTO.class,
                            lm.lineMaterialId.as("lineMaterialId"),
                            lm.materialName.as("materialName"),
                            lm.totalQty.castToNum(Integer.class).as("totalQty"),
                            lm.unit.as("unit"),
                            lm.requiredQtyPerUnit.castToNum(Double.class).as("requiredQtyPerUnit"),
                            lm.processStep.as("processStep")
                    ))
                    .from(lm)
                    .where(lm.workOrders.lotNo.eq(workOrder.getLotNo()))
                    .fetch();

            workOrder.setLineMaterials(lineMaterials);
        }

        return new PageImpl<>(workOrders, pageable, totalCount);
    }

    // 특정 작업지시서 조회
    public WorkFindAllDTO findByCodeWorkOrder(String lotNo) {
        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

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

        List<LineMaterialDTO> lineMaterials = queryFactory
                .select(Projections.fields(LineMaterialDTO.class,
                        lm.lineMaterialId.as("lineMaterialId"),
                        lm.materialName.as("materialName"),
                        lm.totalQty.castToNum(Integer.class).as("totalQty"),
                        lm.unit.as("unit"),
                        lm.requiredQtyPerUnit.castToNum(Double.class).as("requiredQtyPerUnit"),
                        lm.processStep.as("processStep")
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
        // 사원 정보 조회
        Employee employee = employeeRepository.findById(work.getEmpId())
                .orElseThrow(() -> new IllegalArgumentException("사원을 찾을 수 없습니다: " + work.getEmpId()));

        // 제품, 생산 라인, 진행 상태 조회
        PlanLine planLine = planLineRepository.findById(work.getPlanLineId())
                .orElseThrow(() -> new IllegalArgumentException("생산라인을 찾을 수 없습니다: " + work.getPlanLineId()));
        PlanProduct planProduct = planProductRepository.findById(work.getPlanProductId())
                .orElseThrow(() -> new IllegalArgumentException("생산제품을 찾을 수 없습니다: " + work.getPlanProductId()));
        processTracking processTracking = processTrackingRepository.findById(work.getTrackingId())
                .orElseThrow(() -> new IllegalArgumentException("진행상태를 찾을 수 없습니다: " + work.getTrackingId()));
        PlanMst planMst = workPlanMstRepository.findById(work.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("생산계획을 찾을 수 없습니다: " + work.getPlanId()));

        // 작업지시서 객체 생성 및 설정
        WorkOrders workOrder = modelMapper.map(work, WorkOrders.class);
        workOrder.setPlanProduct(planProduct);
        workOrder.setEmployee(employee);
        workOrder.setPlanLine(planLine);
        workOrder.setProcessTracking(processTracking);
        workOrder.setPlanMst(planMst);

        // 가장 최신 작업지시서 LOT 번호 조회 및 새 LOT 번호 생성
        String maxWorkOrderId = workRepository.findTopByOrderByLotNoDesc()
                .map(WorkOrders::getLotNo)
                .orElse(null);
        String newWorkOrderId = generateNewWorkOrderId(maxWorkOrderId);
        workOrder.setLotNo(newWorkOrderId);  // 새 LOT 번호 설정

        // 자재 목록 생성 및 계산
        List<LineMaterial> lineMaterials = work.getLineMaterials().stream()
                .map(lineMaterialDTO -> {
                    LineMaterial lineMaterial = modelMapper.map(lineMaterialDTO, LineMaterial.class);
                    lineMaterial.setWorkOrders(workOrder);
                    return lineMaterial;
                })
                .collect(Collectors.toList());

        // 작업지시서에 자재 목록 설정
        workOrder.setLineMaterial(lineMaterials);

        // 작업지시서 저장
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

    // 직원전체조회
    public List<EmployeeDTO> employee() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(employee -> modelMapper.map(employee, EmployeeDTO.class)).collect(Collectors.toList());
    }

    // 맥주레시피
    public Map<String, Map<String, List<BeerRecipesDTO>>> beerRecipes() {
        QBeerRecipesWork br = QBeerRecipesWork.beerRecipesWork;
        QMaterialWarehouseWork mw = QMaterialWarehouseWork.materialWarehouseWork;

        List<BeerRecipesDTO> recipes = queryFactory
                .select(Projections.fields(BeerRecipesDTO.class,
                        br.id.as("id"),
                        br.beerName.as("beerName"),
                        br.quantity.as("quantity"),
                        br.processStep.as("processStep"),
                        br.material.materialId.as("materialId"), // ✅ 추가
                        mw.materialName.as("materialName"),
                        mw.materialType.as("materialType")
                ))
                .from(br)
                .leftJoin(mw).on(br.material.materialId.eq(mw.materialId)) // ✅ leftJoin 변경
                .fetch();

        // ✅ 맥주별 + 공정별로 그룹화
        return recipes.stream()
                .collect(Collectors.groupingBy(
                        BeerRecipesDTO::getBeerName,
                        Collectors.groupingBy(BeerRecipesDTO::getProcessStep)
                ));
    }

    // 생산계획정보
    public List<PlanInfoDTO> workOrderPlanInfo() {
        QPlanMst pm = QPlanMst.planMst;
        QPlanProduct pp = QPlanProduct.planProduct;
        QPlanLine pl = QPlanLine.planLine;

        return queryFactory
                .select(Projections.fields(PlanInfoDTO.class,
                        pm.planId.as("planId"),
                        pm.status.as("status"),
                        pp.productName.as("productName"),
                        pl.lineNo.as("lineNo"),
                        pl.planQty.as("planQty"),
                        pl.startDate.as("startDate"),
                        pl.endDate.as("endDate")
                ))
                .from(pm)
                .join(pp).on(pm.planId.eq(pp.planId))
                .join(pl).on(pp.planProductId.eq(pl.planProductId))
                .where(pm.status.eq("확정"))
                .fetch();
    }

}
