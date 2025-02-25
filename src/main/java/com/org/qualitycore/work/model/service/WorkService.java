package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.LineMaterialDTO;
import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.entity.*;
import com.org.qualitycore.work.model.repository.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        // 사원 정보 조회
        Employee employee = employeeRepository.findById(work.getEmpId())
                .orElseThrow(() -> new IllegalArgumentException("사원을 찾을 수 없습니다: " + work.getEmpId()));

        // 작업지시서 객체 생성 및 매핑
        WorkOrders workOrder = modelMapper.map(work, WorkOrders.class);

        // 제품, 라인, 진행상태 설정
        PlanLine planLine = planLineRepository.findById(work.getPlanLineId())
                .orElseThrow(() -> new IllegalArgumentException("생산라인을 찾을 수 없습니다: " + work.getPlanLineId()));
        PlanProduct planProduct = planProductRepository.findById(work.getPlanProductId())
                .orElseThrow(() -> new IllegalArgumentException("생산제품을 찾을 수 없습니다: " + work.getPlanProductId()));
        processTracking processTracking = processTrackingRepository.findById(work.getTrackingId())
                .orElseThrow(() -> new IllegalArgumentException("진행상태를 찾을 수 없습니다: " + work.getTrackingId()));

        workOrder.setPlanProduct(planProduct);
        workOrder.setEmployee(employee);
        workOrder.setPlanLine(planLine);
        workOrder.setProcessTracking(processTracking);

        // LineMaterial 목록 생성 및 매핑
        List<LineMaterial> lineMaterials = work.getLineMaterials().stream()
                .map(lineMaterialDTO -> {
                    LineMaterial lineMaterial = modelMapper.map(lineMaterialDTO, LineMaterial.class);
                    lineMaterial.setLineMaterialId(lineMaterialDTO.getLineMaterialId());  // 명시적으로 ID 설정
                    lineMaterial.setWorkOrders(workOrder);  // WorkOrders 설정
                    return lineMaterial;
                })
                .collect(Collectors.toList());

        // 작업지시서에 자재 목록 설정
        workOrder.setLineMaterial(lineMaterials);

        // 작업지시서 저장
        workRepository.save(workOrder);
    }

    @Transactional
    public void workOrderDelete(String lotNo) {

        workRepository.deleteById(lotNo);

        modelMapper.map(lotNo, WorkFindAllDTO.class);
    }
}
