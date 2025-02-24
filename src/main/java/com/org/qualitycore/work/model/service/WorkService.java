package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.LineMaterialDTO;
import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.entity.*;
import com.org.qualitycore.work.model.repository.WorkRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Schema(description = "작업지시서 관련 Service")
public class WorkService {

        private final WorkRepository workRepository;
        private final ModelMapper mapper;
        private final JPAQueryFactory queryFactory;


    // 작업지시서 전체조회
    public List<WorkFindAllDTO> findAllWorkOrders() {
        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

        // 작업지시서 DTO 목록을 담을 리스트
        List<WorkFindAllDTO> result = queryFactory
                .select(Projections.fields(WorkFindAllDTO.class,
                        wo.lotNo.as("lotNo"),                         // 작업지시서 ID
                        pp.productName.as("productName"),              // 제품명
                        pl.lineNo.as("lineNo"),                       // 생산라인 번호
                        pl.planQty.as("planQty"),                     // 배정 수량
                        pl.startDate.as("startDate"),                  // 생산 시작일
                        pl.endDate.as("endDate"),                     // 생산 종료일
                        pt.processStatus.as("processStatus"),          // 현재 공정
                        pt.processName.as("processName"),              // 공정명
                        e.workTeam.as("workTeam")                      // 작업조
                ))
                .from(wo)
                .join(pl).on(wo.planLineId.eq(pl.planLineId))
                .join(pp).on(pl.planProductId.eq(pp.planProductId))
                .join(e).on(wo.empId.eq(e.empId))
                .join(pt).on(wo.lotNo.eq(pt.lotNo))
                .fetch();  // 여러 작업지시서 반환

        // 자재 정보는 따로 쿼리로 가져와서 리스트에 추가
        for (WorkFindAllDTO workFindAllDTO : result) {
            List<LineMaterialDTO> lineMaterials = queryFactory
                    .select(Projections.fields(LineMaterialDTO.class,
                            lm.materialName.as("materialName"),
                            lm.materialType.as("materialType"),
                            lm.unit.as("unit"),
                            lm.requiredQtyPerUnit.as("requiredQtyPerUnit"),
                            lm.pricePerUnit.as("pricePerUnit"),
                            lm.totalCost.as("totalCost")
                    ))
                    .from(lm)
                    .join(pl).on(lm.planLineId.eq(pl.planLineId))
                    .join(pp).on(lm.planProductId.eq(pp.planProductId))
                    .where(lm.planLineId.eq(pl.planLineId).and(pp.planProductId.eq(pp.planProductId)))
                    .fetch();

            // 작업지시서 DTO에 자재 목록을 세팅
            workFindAllDTO.setLineMaterials(lineMaterials);
        }

        return result;
    }


    public WorkFindAllDTO findByCodeWorkOrder(String lotNo) {

        QWorkOrders wo = QWorkOrders.workOrders;
        QPlanLine pl = QPlanLine.planLine;
        QPlanProduct pp = QPlanProduct.planProduct;
        QLineMaterial lm = QLineMaterial.lineMaterial;
        QEmployee e = QEmployee.employee;
        QprocessTracking pt = QprocessTracking.processTracking;

        // 작업지시서 DTO를 하나만 반환하기 위한 쿼리
        WorkFindAllDTO result = queryFactory
                .select(Projections.fields(WorkFindAllDTO.class,
                        wo.lotNo.as("lotNo"),                         // 작업지시서 ID
                        pp.productName.as("productName"),              // 제품명
                        pl.lineNo.as("lineNo"),                       // 생산라인 번호
                        pl.planQty.as("planQty"),                     // 배정 수량
                        pl.startDate.as("startDate"),                  // 생산 시작일
                        pl.endDate.as("endDate"),                     // 생산 종료일
                        pt.processStatus.as("processStatus"),          // 현재 공정
                        pt.processName.as("processName"),              // 공정명
                        e.workTeam.as("workTeam")                      // 작업조
                ))
                .from(wo)
                .join(pl).on(wo.planLineId.eq(pl.planLineId))
                .join(pp).on(pl.planProductId.eq(pp.planProductId))
                .join(e).on(wo.empId.eq(e.empId))
                .join(pt).on(wo.lotNo.eq(pt.lotNo))
                .where(wo.lotNo.eq(lotNo))  // 특정 작업지시서 번호로 조회
                .fetchOne();  // 하나의 작업지시서만 반환

        // 자재 정보는 따로 쿼리로 가져와서 리스트에 추가
        if (result != null) {
            List<LineMaterialDTO> lineMaterials = queryFactory
                    .select(Projections.fields(LineMaterialDTO.class,
                            lm.materialName.as("materialName"),
                            lm.materialType.as("materialType"),
                            lm.unit.as("unit"),
                            lm.requiredQtyPerUnit.as("requiredQtyPerUnit"),
                            lm.pricePerUnit.as("pricePerUnit"),
                            lm.totalCost.as("totalCost")
                    ))
                    .from(lm)
                    .join(pl).on(lm.planLineId.eq(pl.planLineId))
                    .join(pp).on(lm.planProductId.eq(pp.planProductId))
                    .where(lm.planLineId.eq(pl.planLineId).and(pp.planProductId.eq(pp.planProductId)))
                    .fetch();

            // 작업지시서 DTO에 자재 목록을 세팅한다.
            result.setLineMaterials(lineMaterials);
        }
        return result;
    }
}

