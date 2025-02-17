package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.entity.PlanMst;
import com.org.qualitycore.productionPlan.model.entity.PlanProduct;
import com.org.qualitycore.productionPlan.model.entity.QPlanMst;
import com.org.qualitycore.productionPlan.model.entity.QProductionPlan;
import com.org.qualitycore.productionPlan.model.repository.PlanMstRepository;
import com.org.qualitycore.productionPlan.model.repository.PlanProductRepository;
import com.org.qualitycore.productionPlan.model.repository.PlanRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMstRepository planMstRepository;
    private final PlanProductRepository planProductRepository;

    public List<ProductionPlanDTO> getAllProductionPlans(LocalDate startDate, LocalDate endDate, String status) {
        return planRepository.findProductionPlans(startDate, endDate, status);
    }

    @Transactional
    public void saveProductionPlan(ProductionPlanDTO dto) {
        // 🟢 planYm (YYYY-MM-DD) -> LocalDate 그대로 저장 (조회 영향 없음)
        LocalDate planYm = dto.getPlanYm();

        // 🟢 계획 마스터 저장
        PlanMst planMst = new PlanMst();
        planMst.setPlanYm(planYm);
        planMst = planMstRepository.save(planMst); // planId 자동 생성됨

        // 🟢 새로운 PLAN_PRODUCT_ID 생성
        String newPlanProductId = generateNewPlanProductId();

        // 🟢 생산 계획 제품 저장
        PlanProduct planProduct = new PlanProduct();
        planProduct.setPlanProductId(newPlanProductId);
        planProduct.setPlanMst(planMst); // planId 연동
        planProduct.setProductId(dto.getProductId());
        planProduct.setProductName(dto.getProductName());
        planProduct.setPlanQty(dto.getPlanQty());

        planProductRepository.save(planProduct);
    }


    // 새로운 PLAN_ID 생성 (PL00001, PL00002...)
    private String generateNewPlanId() {
        String maxId = planMstRepository.findMaxPlanId();
        if (maxId == null) {
            return "PL00001"; // 첫 번째 ID
        }
        int numericPart = Integer.parseInt(maxId.substring(2)); // "PL00005" -> 5
        numericPart++; // 6으로 증가
        return String.format("PL%05d", numericPart); // "PL00006" 형식으로 변환
    }

    // 새로운 PLAN_PRODUCT_ID 생성 (PP00001, PP00002...)
    private String generateNewPlanProductId() {
        String maxId = planProductRepository.findMaxPlanProductId();
        if (maxId == null) {
            return "PP00001"; // 첫 번째 ID
        }
        int numericPart = Integer.parseInt(maxId.substring(2)); // "PP00005" -> 5
        numericPart++; // 6으로 증가
        return String.format("PP%05d", numericPart); // "PP00006" 형식으로 변환
    }
}