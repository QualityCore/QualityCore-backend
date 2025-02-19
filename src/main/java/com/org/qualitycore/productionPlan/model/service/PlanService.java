package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.exception.ResourceNotFoundException;
import com.org.qualitycore.productionPlan.model.dto.PlanLineDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductBomDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.entity.PlanLine;
import com.org.qualitycore.productionPlan.model.entity.PlanMst;
import com.org.qualitycore.productionPlan.model.entity.PlanProduct;
import com.org.qualitycore.productionPlan.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.org.qualitycore.productionPlan.model.entity.QProductBom.productBom;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMstRepository planMstRepository;
    private final PlanProductRepository planProductRepository;
    private final ProductBomRepository productBomRepository;
    private final PlanLineRepository planLineRepository;

    public List<ProductionPlanDTO> getAllProductionPlans(LocalDate startDate, LocalDate endDate, String status) {
        return planRepository.findProductionPlans(startDate, endDate, status);
    }

    @Transactional
    public void saveProductionPlan(ProductionPlanDTO dto) {

        System.out.println("📌 saveProductionPlan() 실행됨: " + dto);
        System.out.flush();
        // 🟢 planYm (YYYY-MM-DD) -> LocalDate 그대로 저장 (조회 영향 없음)
        LocalDate planYm = dto.getPlanYm();

        // 🟢 새로운 PLAN_ID 생성
        String newPlanId = generateNewPlanId();
        System.out.println("Generated Plan ID: " + newPlanId);

        // 🟢 계획 마스터 저장
        PlanMst planMst = new PlanMst();
        planMst.setPlanId(newPlanId);
        planMst.setPlanYm(planYm);
        planMst.setCreatedBy("SYSTEM");
        planMst.setStatus("미확정");

        planMst = planMstRepository.save(planMst); // ✅ 저장

        //  새로운 PLAN_PRODUCT_ID 생성
        String newPlanProductId = generateNewPlanProductId();
        System.out.println("Generated Plan Product ID: " + newPlanProductId);
        System.out.flush();

        if (newPlanProductId == null) {
            throw new RuntimeException("Generated Plan Product ID is null");
        }

        //  생산 계획 제품 저장
        PlanProduct planProduct = new PlanProduct();
        planProduct.setPlanProductId(newPlanProductId);
        planProduct.setPlanMst(planMst); // planId 연동
        planProduct.setProductId(dto.getProductId()); // dto에서 받아오기
        planProduct.setProductName(dto.getProductName());
        planProduct.setPlanQty(dto.getPlanQty());

        planProductRepository.save(planProduct);
        System.out.println("Plan Product saved successfully.");
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

    public ProductBomDTO getProductStandard(String productId) {
        return productBomRepository.findByProductId(productId)
                .map(ProductBomDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("제품 정보를 찾을 수 없습니다."));
    }

    public List<ProductBomDTO> getAllProducts() {
        return productBomRepository.findAll().stream()
                .map(ProductBomDTO::fromEntity)
                .collect(Collectors.toList());


    }

//    public List<PlanLineDTO> getProductionLines(String planProductId) {
//        return planLineRepository.findProductionLinesByProductId(planProductId)
//                .stream()
//                .map(PlanLineDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void saveProductionLines(List<PlanLineDTO> planLineDTOs) {
//        List<PlanLine> planLines = planLineDTOs.stream().map(PlanLineDTO::toEntity).toList();
//        planLineRepository.saveAll(planLines);
//
//    }
}
