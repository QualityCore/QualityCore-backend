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
    public String saveProductionPlan(ProductionPlanDTO dto) {
        System.out.println(" Step1 - 생산 계획 저장 시작: " + dto);

        // ✅ planProductId 생성 후 반환하도록 수정
        String newPlanProductId = generateNewPlanProductId();

        // ✅ 계획 마스터 저장
        String newPlanId = generateNewPlanId();
        PlanMst planMst = new PlanMst();
        planMst.setPlanId(newPlanId);
        planMst.setPlanYm(dto.getPlanYm());
        planMst.setCreatedBy("SYSTEM");
        planMst.setStatus("미확정");
        planMst = planMstRepository.save(planMst);

        // ✅ 생산 계획 제품 저장
        PlanProduct planProduct = new PlanProduct();
        planProduct.setPlanProductId(newPlanProductId);
        planProduct.setPlanMst(planMst);
        planProduct.setProductId(dto.getProductId());
        planProduct.setProductName(dto.getProductName());
        planProduct.setPlanQty(dto.getPlanQty());
        planProductRepository.save(planProduct);

        System.out.println("✅ [PlanService] Step1 - 저장 완료! planProductId: " + newPlanProductId);

        return newPlanProductId; // ✅ 생성된 planProductId 반환
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
    public List<PlanLineDTO> getProductionLines(String planProductId) {
        System.out.println(" Step2에서 받은 planProductId: " + planProductId);

        // ✅ planProductId가 null이 아닌지 확인
        if (planProductId == null || planProductId.isEmpty()) {
            System.out.println("planProductId가 없음! 조회 불가 비상!!!");
            return List.of(); // 빈 리스트 반환
        }

        List<PlanLine> planLines = planLineRepository.findProductionLinesByPlanProductId(planProductId); // ✅ 올바른 조회 방식

        System.out.println(" 조회된 생산 라인 개수: " + planLines.size());
        if (planLines.isEmpty()) {
            System.out.println("해당 제품의 생산 라인 배정 데이터가 없음!!!");
        }

        return planLines.stream().map(PlanLineDTO::fromEntity).collect(Collectors.toList());
    }


    @Transactional
    public void saveProductionLines(List<PlanLineDTO> planLineDTOs) {
        List<PlanLine> planLines = planLineDTOs.stream().map(PlanLineDTO::toEntity).toList();
        planLineRepository.saveAll(planLines);

    }
}
