package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.exception.ResourceNotFoundException;
import com.org.qualitycore.productionPlan.model.dto.PlanLineDTO;
import com.org.qualitycore.productionPlan.model.dto.PlanMaterialDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductBomDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.entity.*;
import com.org.qualitycore.productionPlan.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.org.qualitycore.productionPlan.model.entity.QPlanMaterial.planMaterial;
import static com.org.qualitycore.productionPlan.model.entity.QProductBom.productBom;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMstRepository planMstRepository;
    private final PlanProductRepository planProductRepository;
    private final ProductBomRepository productBomRepository;
    private final PlanLineRepository planLineRepository;
    private final BeerRecipeRepository beerRecipeRepository;
    private final MaterialWarehouseRepository materialWarehouseRepository;
    private final PlanMaterialRepository planMaterialRepository;
    private final MaterialRequestRepository materialRequestRepository;

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

        System.out.println(" Step1 - 저장 완료! planProductId: " + newPlanProductId);

        return newPlanProductId; //  생성된 planProductId 반환
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

        //  planProductId가 null이 아닌지 확인
        if (planProductId == null || planProductId.isEmpty()) {
            System.out.println("planProductId가 없음! 조회 불가 비상!!!");
            return List.of(); // 빈 리스트 반환
        }

        List<PlanLine> planLines = planLineRepository.findProductionLinesByPlanProductId(planProductId);

        System.out.println(" 조회된 생산 라인 개수: " + planLines.size());
        if (planLines.isEmpty()) {
            System.out.println("해당 제품의 생산 라인 배정 데이터가 없음 비상!!!");
        }

        return planLines.stream().map(PlanLineDTO::fromEntity).collect(Collectors.toList());
    }


    @Transactional
    public void saveProductionLines(List<PlanLineDTO> planLineDTOs) {
        List<PlanLine> planLines = planLineDTOs.stream().map(PlanLineDTO::toEntity).toList();
        planLineRepository.saveAll(planLines);

    }

    @Transactional
    public Map<String, Object> calculateMaterialRequirements(ProductionPlanDTO productionPlanDTO) {
        Map<String, Object> result = new HashMap<>();
        Map<String, PlanMaterialDTO> materialMap = new LinkedHashMap<>();

        for (ProductionPlanDTO productDTO : productionPlanDTO.getProducts()) {
            List<BeerRecipe> beerRecipes = beerRecipeRepository.findByBeerName(productDTO.getProductName());

            for (BeerRecipe recipe : beerRecipes) {
                MaterialWarehouse material = materialWarehouseRepository.findById(recipe.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("자재를 찾을 수 없습니다: " + recipe.getMaterialId()));

                // 계획 소요량 계산
                Double recipeQuantity = recipe.getQuantity() != null ? recipe.getQuantity() : 0.0;
                Integer planQty = productDTO.getPlanQty() != null ? productDTO.getPlanQty() : 0;
                Double totalQuantity = Math.round(recipeQuantity * planQty * 10000.0) / 10000.0;

                // 키: 맥주 이름 + 자재 ID
                String materialKey = productDTO.getProductName() + "-" + material.getMaterialId();

                PlanMaterialDTO planMaterial = materialMap.computeIfAbsent(materialKey, k -> {
                    PlanMaterialDTO newMaterial = new PlanMaterialDTO();
                    newMaterial.setMaterialId(material.getMaterialId());
                    newMaterial.setMaterialName(material.getMaterialName());
                    newMaterial.setMaterialType(material.getMaterialType());
                    newMaterial.setUnit(material.getUnit());
                    newMaterial.setStdQty(0.0);
                    newMaterial.setPlanQty(0.0);
                    newMaterial.setCurrentStock(material.getCurrentStock()); // 원본 재고 유지
                    newMaterial.setBeerName(productDTO.getProductName());
                    return newMaterial;
                });

                // 소요량 누적
                planMaterial.setStdQty(planMaterial.getStdQty() + recipeQuantity);
                planMaterial.setPlanQty(planMaterial.getPlanQty() + totalQuantity);
            }
        }

        // 상태 및 부족 수량 결정
        Map<String, Double> totalPlanQtyMap = new HashMap<>();
        Map<String, Double> totalCurrentStockMap = new HashMap<>();

        // 1단계: 전체 자재별 총 소요량과 총 재고 계산
        for (PlanMaterialDTO material : materialMap.values()) {
            String materialId = material.getMaterialId();

            totalPlanQtyMap.merge(materialId, material.getPlanQty(), Double::sum);
            totalCurrentStockMap.merge(materialId, material.getCurrentStock(), Double::max);
        }

        // 2단계: 상태 결정
        for (PlanMaterialDTO material : materialMap.values()) {
            String materialId = material.getMaterialId();
            Double totalPlanQty = totalPlanQtyMap.get(materialId);
            Double totalCurrentStock = totalCurrentStockMap.get(materialId);

            System.out.println("상태 결정 디버그: " +
                    "맥주=" + material.getBeerName() +
                    ", 자재명=" + material.getMaterialName() +
                    ", 총 계획 소요량=" + totalPlanQty +
                    ", 총 현재 재고=" + totalCurrentStock);

            if (totalPlanQty > totalCurrentStock) {
                material.setStatus("부족");
                material.setShortageQty(totalPlanQty - totalCurrentStock);

                System.out.println(" 부족 자재 발견: " +
                        "맥주=" + material.getBeerName() +
                        ", 자재명=" + material.getMaterialName() +
                        ", 부족량=" + material.getShortageQty());
            } else {
                material.setStatus("충분");
                material.setShortageQty(0.0);
            }
        }

        // 원자재와 포장재 분류
        List<PlanMaterialDTO> rawMaterials = materialMap.values().stream()
                .filter(m -> !m.getMaterialType().equals("부자재"))
                .collect(Collectors.toList());

        List<PlanMaterialDTO> packagingMaterials = materialMap.values().stream()
                .filter(m -> m.getMaterialType().equals("부자재"))
                .collect(Collectors.toList());

        result.put("rawMaterials", rawMaterials);
        result.put("packagingMaterials", packagingMaterials);

        return result;
    }





    @Transactional
    public String saveCompletePlan(ProductionPlanDTO completeProductionPlan) {
        // Step 1: 생산 계획 마스터 및 제품 저장
        String planProductId = saveProductionPlan(completeProductionPlan);

        // Step 2: 생산 라인 저장
        if (completeProductionPlan.getAllocatedLines() != null) {
            List<PlanLine> planLines = completeProductionPlan.getAllocatedLines().stream()
                    .map(lineDTO -> {
                        PlanLine planLine = lineDTO.toEntity();
                        PlanProduct planProduct = new PlanProduct();
                        planProduct.setPlanProductId(planProductId);
                        planLine.setPlanProduct(planProduct);
                        return planLine;
                    })
                    .collect(Collectors.toList());

            planLineRepository.saveAll(planLines);
        }

        // Step 3: 자재 소요량 저장
        if (completeProductionPlan.getMaterials() != null) {
            List<PlanMaterial> planMaterials = completeProductionPlan.getMaterials().stream()
                    .map(materialDTO -> {
                        PlanMaterial planMaterial = materialDTO.toEntity();
                        PlanProduct planProduct = new PlanProduct();
                        planProduct.setPlanProductId(planProductId);
                        planMaterial.setPlanProduct(planProduct);
                        planMaterial.setPlanMaterialId(generateNewPlanMaterialId());
                        return planMaterial;
                    })
                    .collect(Collectors.toList());

            planMaterialRepository.saveAll(planMaterials);
        }

        // Step 4: 자재 구매 신청 저장
        if (completeProductionPlan.getMaterialRequests() != null) {
            List<MaterialRequest> materialRequests = completeProductionPlan.getMaterialRequests().stream()
                    .map(requestDTO -> {
                        MaterialRequest materialRequest = requestDTO.toEntity();
                        materialRequest.setRequestId(generateNewMaterialRequestId());
                        PlanMaterial planMaterial = new PlanMaterial();
                        planMaterial.setPlanMaterialId(requestDTO.getPlanMaterialId());
                        materialRequest.setPlanMaterial(planMaterial);
                        return materialRequest;
                    })
                    .collect(Collectors.toList());

            materialRequestRepository.saveAll(materialRequests);
        }

        return planProductId;
    }

    private String generateNewMaterialRequestId() {
        String maxId = materialRequestRepository.findMaxRequestId(); // 이 메서드를 Repository에 추가해야 합니다
        if (maxId == null) {
            return "MR00001"; // 첫 번째 ID
        }
        int numericPart = Integer.parseInt(maxId.substring(2)); // "MR00005" -> 5
        numericPart++; // 6으로 증가
        return String.format("MR%05d", numericPart); // "MR00006" 형식으로 변환
    }

    private String generateNewPlanMaterialId() {
        String maxId = planMaterialRepository.findMaxPlanMaterialId();
        if (maxId == null) {
            return "PM00001"; // 첫 번째 ID
        }
        int numericPart = Integer.parseInt(maxId.substring(2)); // "PM00005" -> 5
        numericPart++; // 6으로 증가
        return String.format("PM%05d", numericPart); // "PM00006" 형식으로 변환
    }


}