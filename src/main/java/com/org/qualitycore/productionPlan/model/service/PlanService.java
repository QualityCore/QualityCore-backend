package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;

import com.org.qualitycore.productionPlan.model.entity.QPlanMst;
import com.org.qualitycore.productionPlan.model.entity.QProductionPlan;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final JPAQueryFactory queryFactory;

    public List<ProductionPlanDTO> getAllProductionPlans(LocalDate planYm, String status) {
        QPlanMst planMst = QPlanMst.planMst;
        QProductionPlan productionPlan = QProductionPlan.productionPlan;

        return queryFactory
                .select(Projections.fields(ProductionPlanDTO.class,
                        planMst.planYm.as("planYm"),
                        productionPlan.productId.as("productId"),
                        productionPlan.productName.as("productName"),
                        productionPlan.sizeSpec.as("sizeSpec"),
                        productionPlan.planQty.as("planQty"),
                        planMst.status.as("status")

                ))
                .from(productionPlan)
                .join(planMst).on(productionPlan.planId.eq(planMst.planId))
                .where(
                        planMst.planYm.eq(planYm),
                        status != null ? planMst.status.eq(status) : null
                )
                .fetch();




    }
}
