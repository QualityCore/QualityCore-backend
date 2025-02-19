package com.org.qualitycore.productionPlan.model.repository;

import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.entity.QPlanMst;
import com.org.qualitycore.productionPlan.model.entity.QProductionPlan;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlanRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProductionPlanDTO> findProductionPlans(LocalDate startDate, LocalDate endDate, String status) {
        QPlanMst planMst = QPlanMst.planMst;
        QProductionPlan productionPlan = QProductionPlan.productionPlan;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(planMst.planYm.between(startDate, endDate)); // LocalDate 비교

        // status 필터링 (null 체크 개선)
        if (status != null && !status.isBlank()) {
            whereClause.and(planMst.status.eq(status));
        }

        return queryFactory
                .select(Projections.fields(ProductionPlanDTO.class,
                        planMst.planYm.as("planYm"), // LocalDate 그대로 사용
                        productionPlan.productId.as("productId"),
                        productionPlan.productName.as("productName"),
                        productionPlan.sizeSpec.as("sizeSpec"),
                        productionPlan.planQty.as("planQty"),
                        planMst.status.as("status")
                ))
                .from(productionPlan)
                .join(planMst).on(productionPlan.planId.eq(planMst.planId))
                .where(whereClause)
                .fetch();
    }
}
