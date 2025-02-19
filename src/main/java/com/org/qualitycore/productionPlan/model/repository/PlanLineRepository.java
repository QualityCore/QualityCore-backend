package com.org.qualitycore.productionPlan.model.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.org.qualitycore.productionPlan.model.entity.PlanLine;
import com.org.qualitycore.productionPlan.model.entity.QPlanLine;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlanLineRepository {

    private final JPAQueryFactory queryFactory;
    private final SpringDataPlanLineRepository springDataPlanLineRepository;

    // 특정 제품의 생산라인 배정 정보 조회 (QueryDSL)
    public List<PlanLine> findProductionLinesByProductId(String planProductId) {
        return queryFactory
                .selectFrom(QPlanLine.planLine)
                .where(QPlanLine.planLine.planProduct.planProductId.eq(planProductId))
                .fetch();
    }

    // JPA 기본 저장 메서드 활용
    public void saveAll(List<PlanLine> planLines) {
        springDataPlanLineRepository.saveAll(planLines);
    }
}
