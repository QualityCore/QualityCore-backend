package com.org.qualitycore.productionPlan.model.repository;

import com.org.qualitycore.productionPlan.model.entity.PlanMaterial;
import com.org.qualitycore.productionPlan.model.entity.QPlanMaterial;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.org.qualitycore.productionPlan.model.entity.PlanProduct;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlanMaterialRepository{
    private final JPAQueryFactory queryFactory;
    private final SpringDataPlanMaterialRepository springDataPlanMaterialRepository;


    // 최대 PlanMaterialId 찾는 메서드 추가
    public String findMaxPlanMaterialId() {
        String maxId = queryFactory
                .select(QPlanMaterial.planMaterial.planMaterialId.max())
                .from(QPlanMaterial.planMaterial)
                .fetchOne();

        return maxId;
    }

    public void saveAll(List<PlanMaterial> planMaterials) {
        springDataPlanMaterialRepository.saveAll(planMaterials);
    }


}
