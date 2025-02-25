package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.exception.ResourceNotFoundException;
import com.org.qualitycore.productionPlan.model.dto.*;
import com.org.qualitycore.productionPlan.model.entity.*;
import com.org.qualitycore.productionPlan.model.repository.*;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;
    public List<ProductionPlanDTO> getAllProductionPlans(LocalDate startDate, LocalDate endDate, String status) {
        return planRepository.findProductionPlans(startDate, endDate, status);
    }

    @Transactional
    public String saveProductionPlan(ProductionPlanDTO dto) {

        System.out.println("Received DTO in saveProductionPlan: " + dto);


        if (dto.getPlanYm() == null) {
            dto.setPlanYm(LocalDate.now());  // ✅ 기본값 설정
        }

        System.out.println("PlanYm from DTO: " + dto.getPlanYm());

        String newPlanProductId = generateNewPlanProductId();

        String newPlanId = generateNewPlanId();
        PlanMst planMst = new PlanMst();
        planMst.setPlanId(newPlanId);
        planMst.setPlanYm(dto.getPlanYm());
        planMst.setCreatedBy("SYSTEM");
        planMst.setStatus("미확정");

        System.out.println("PlanMst before save: " + planMst);
        planMst = planMstRepository.save(planMst);

        PlanProduct planProduct = new PlanProduct();
        planProduct.setPlanProductId(newPlanProductId);
        planProduct.setPlanMst(planMst);
        planProduct.setProductId(dto.getProductId());
        planProduct.setProductName(dto.getProductName());
        planProduct.setPlanQty(dto.getPlanQty());

        System.out.println("PlanProduct before save: " + planProduct);
        planProductRepository.save(planProduct);

        System.out.println(" Step1 - 저장 완료! planProductId: " + newPlanProductId);

        return newPlanProductId;
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

    private String generateNewPlanLineId() {
        String maxId = planLineRepository.findMaxPlanLineId();
        System.out.println("🔍 현재 DB에서 가장 큰 PLAN_LINE_ID: " + maxId);

        if (maxId == null) {
            return "LN00001"; // 첫 번째 ID
        }
        int numericPart = Integer.parseInt(maxId.substring(2)); // "LN00005" -> 5
        numericPart++; // 6으로 증가
        return String.format("LN%05d", numericPart); // "LN00006" 형식으로 변환
    }


    // 새로운 PLAN_PRODUCT_ID 생성 (PP00001, PP00002...)
    private String generateNewPlanProductId() {
        String maxId = planProductRepository.findMaxPlanProductId();
        System.out.println("🔍 현재 DB에서 가장 큰 PLAN_PRODUCT_ID: " + maxId);

        if (maxId == null || maxId.isEmpty()) {
            System.out.println("🚀 첫 번째 PLAN_PRODUCT_ID 생성: PP00001");
            return "PP00001"; // 첫 번째 ID
        }

        int numericPart = Integer.parseInt(maxId.substring(2)); // "PP00003" -> 3
        numericPart++; // 4로 증가
        String newId = String.format("PP%05d", numericPart); // "PP00004" 형식으로 변환

        System.out.println("🚀 새롭게 생성된 PLAN_PRODUCT_ID: " + newId);
        return newId;
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

//            System.out.println("상태 결정 디버그: " +
//                    "맥주=" + material.getBeerName() +
//                    ", 자재명=" + material.getMaterialName() +
//                    ", 총 계획 소요량=" + totalPlanQty +
//                    ", 총 현재 재고=" + totalCurrentStock);

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





    private String generateNewMaterialRequestId() {
        String maxId = materialRequestRepository.findMaxRequestId(); // 이 메서드를 Repository에 추가해야 합니다
        if (maxId == null) {
            return "MR00001"; // 첫 번째 ID
        }
        int numericPart = Integer.parseInt(maxId.substring(2)); // "MR00005" -> 5
        numericPart++; // 6으로 증가
        return String.format("MR%05d", numericPart); // "MR00006" 형식으로 변환
    }


    @Transactional
    public List<String> saveCompletePlan(ProductionPlanDTO completeProductionPlan) {
        List<String> planProductIds = new ArrayList<>();

        // ✅ Step 1: PLAN_MST 저장 (생산 계획 마스터)
        String newPlanId = generateNewPlanId();
        PlanMst planMst = new PlanMst();
        planMst.setPlanId(newPlanId);
        planMst.setPlanYm(completeProductionPlan.getPlanYm());
        planMst.setCreatedBy("SYSTEM");
        planMst.setStatus("미확정");

        planMst = planMstRepository.save(planMst);
        System.out.println("🚀 저장된 PLAN_MST ID: " + planMst.getPlanId());

        // ✅ Step 2: PLAN_PRODUCT 저장 (여러 제품 가능)
        Map<String, String> productPlanIdMap = new HashMap<>(); // 제품 ID ↔ PLAN_PRODUCT_ID 매핑
        for (ProductionPlanDTO product : completeProductionPlan.getProducts()) {
            String newPlanProductId = generateNewPlanProductId(); // 새로운 PLAN_PRODUCT_ID 생성

            if (newPlanProductId == null || newPlanProductId.isEmpty()) {
                throw new RuntimeException("🚨 PLAN_PRODUCT_ID 생성 오류: NULL 값이 생성됨");
            }

            PlanProduct planProduct = new PlanProduct();
            planProduct.setPlanProductId(newPlanProductId);
            planProduct.setPlanMst(planMst);  // ✅ PLAN_MST와 연결
            planProduct.setProductId(product.getProductId());
            planProduct.setProductName(product.getProductName());
            planProduct.setPlanQty(product.getPlanQty());
            planProduct.setSizeSpec(product.getSizeSpec());

            System.out.println("✅ PLAN_PRODUCT 저장: " + newPlanProductId + " / " + planProduct.getProductName());
            planProductRepository.save(planProduct);
            planProductIds.add(newPlanProductId);
            productPlanIdMap.put(product.getProductId(), newPlanProductId); // 제품 ID와 PLAN_PRODUCT_ID 매핑 저장
        }



        // ✅ Step 2.5: PLAN_LINE 저장 (생산 라인 배정 정보)
        if (completeProductionPlan.getAllocatedLines() != null && !completeProductionPlan.getAllocatedLines().isEmpty()) {
            System.out.println("🔍 Step 2.5: 생산 라인 저장 시작");
            System.out.println("🔍 전체 생산 라인 리스트 크기: " + completeProductionPlan.getAllocatedLines().size());

            List<PlanLine> planLines = new ArrayList<>();
            for (PlanLineDTO lineDTO : completeProductionPlan.getAllocatedLines()) {
                String relatedPlanProductId = productPlanIdMap.get(lineDTO.getProductId());

                if (relatedPlanProductId == null) {
                    throw new RuntimeException("🚨 생산 라인 저장 오류: " + lineDTO.getProductId() + "에 해당하는 PLAN_PRODUCT_ID 없음");
                }

                // 새로운 PlanLine 객체 생성
                PlanLine planLine = new PlanLine();

                // planLineId 직접 생성
                String newPlanLineId = generateNewPlanLineId();
                planLine.setPlanLineId(newPlanLineId);

                PlanProduct planProduct = new PlanProduct();
                planProduct.setPlanProductId(relatedPlanProductId);
                planProduct.setProductId(lineDTO.getProductId());

                planLine.setPlanProduct(planProduct);
                planLine.setProductId(lineDTO.getProductId());
                planLine.setLineNo(lineDTO.getLineNo());
                planLine.setPlanBatchNo(lineDTO.getPlanBatchNo());
                planLine.setPlanQty(lineDTO.getAllocatedQty());
                planLine.setStartDate(lineDTO.getStartDate());
                planLine.setEndDate(lineDTO.getEndDate());

                // 로그 추가
                System.out.println("저장될 라인 정보: " +
                        "PlanLineId: " + newPlanLineId +
                        ", PlanProductId: " + relatedPlanProductId +
                        ", LineNo: " + planLine.getLineNo() +
                        ", PlanQty: " + planLine.getPlanQty());

                // 즉시 저장
                planLineRepository.save(planLine);
                entityManager.flush(); // 영속성 컨텍스트 즉시 flush
            }

            System.out.println("✅ 최종 생산 라인 저장 개수: " + completeProductionPlan.getAllocatedLines().size());
        }

        // ✅ Step 3: PLAN_MATERIAL 저장 (각 제품의 자재 정보)
        if (completeProductionPlan.getProducts() != null && !completeProductionPlan.getProducts().isEmpty()) {
            System.out.println("🔍 Step 3: 자재 저장 시작");

            // 기존 레시피 기반 자재 저장 로직
            for (ProductionPlanDTO productDTO : completeProductionPlan.getProducts()) {
                String productId = productDTO.getProductId();
                String productName = productDTO.getProductName();
                Integer planQty = productDTO.getPlanQty();

                String relatedPlanProductId = productPlanIdMap.get(productId);
                if (relatedPlanProductId == null) {
                    System.out.println("🚨 경고: " + productId + "에 해당하는 PLAN_PRODUCT_ID 없음");
                    continue;
                }

                // 맥주 레시피 조회
                List<BeerRecipe> beerRecipes = beerRecipeRepository.findByBeerName(productName);

                for (BeerRecipe recipe : beerRecipes) {
                    MaterialWarehouse material = materialWarehouseRepository.findById(recipe.getMaterialId())
                            .orElseThrow(() -> new RuntimeException("자재를 찾을 수 없습니다: " + recipe.getMaterialId()));

                    // 계획 소요량 계산
                    Double recipeQuantity = recipe.getQuantity() != null ? recipe.getQuantity() : 0.0;
                    Double totalQuantity = Math.round(recipeQuantity * planQty * 10000.0) / 10000.0;

                    PlanMaterial planMaterial = new PlanMaterial();
                    planMaterial.setPlanMaterialId(generateNewPlanMaterialId());

                    PlanProduct planProduct = new PlanProduct();
                    planProduct.setPlanProductId(relatedPlanProductId);
                    planProduct.setProductId(productId);

                    planMaterial.setPlanProduct(planProduct);
                    planMaterial.setMaterialId(material.getMaterialId());
                    planMaterial.setMaterialName(material.getMaterialName());
                    planMaterial.setMaterialType(material.getMaterialType());
                    planMaterial.setUnit(material.getUnit());
                    planMaterial.setStdQty(recipeQuantity);
                    planMaterial.setPlanQty(totalQuantity);
                    planMaterial.setCurrentStock(material.getCurrentStock());
                    planMaterial.setBeerName(productName);

                    // 상태 결정
                    if (totalQuantity > material.getCurrentStock()) {
                        planMaterial.setStatus("부족");
                    } else {
                        planMaterial.setStatus("충분");
                    }

                    System.out.println("✅ 저장될 레시피 기반 자재 정보: " + planMaterial);

                    planMaterialRepository.save(planMaterial);
                    entityManager.flush();
                }
            }

            // 원자재(Raw Materials) 저장 로직
            if (completeProductionPlan.getRawMaterials() != null && !completeProductionPlan.getRawMaterials().isEmpty()) {
                System.out.println("🔍 원자재 저장 시작");
                for (PlanMaterialDTO materialDTO : completeProductionPlan.getRawMaterials()) {
                    // 맥주 이름으로 제품 찾기
                    ProductionPlanDTO relatedProduct = completeProductionPlan.getProducts().stream()
                            .filter(p -> p.getProductName().equals(materialDTO.getBeerName()))
                            .findFirst()
                            .orElse(null);

                    if (relatedProduct == null) {
                        System.out.println("🚨 경고: " + materialDTO.getBeerName() + "에 해당하는 제품 정보 없음");
                        continue;
                    }

                    String productId = relatedProduct.getProductId();
                    String relatedPlanProductId = productPlanIdMap.get(productId);

                    if (relatedPlanProductId == null) {
                        System.out.println("🚨 경고: " + productId + "에 해당하는 PLAN_PRODUCT_ID 없음");
                        continue;
                    }

                    PlanMaterial planMaterial = new PlanMaterial();
                    planMaterial.setPlanMaterialId(generateNewPlanMaterialId());

                    PlanProduct planProduct = new PlanProduct();
                    planProduct.setPlanProductId(relatedPlanProductId);
                    planProduct.setProductId(productId);

                    planMaterial.setPlanProduct(planProduct);
                    planMaterial.setMaterialId(materialDTO.getMaterialId());
                    planMaterial.setMaterialName(materialDTO.getMaterialName());
                    planMaterial.setMaterialType(materialDTO.getMaterialType());
                    planMaterial.setUnit(materialDTO.getUnit());
                    planMaterial.setStdQty(materialDTO.getStdQty());
                    planMaterial.setPlanQty(materialDTO.getPlanQty());
                    planMaterial.setCurrentStock(materialDTO.getCurrentStock());
                    planMaterial.setBeerName(materialDTO.getBeerName());
                    planMaterial.setStatus(materialDTO.getStatus());


                    System.out.println("✅ 저장될 원자재 정보: " + planMaterial);

                    planMaterialRepository.save(planMaterial);
                    entityManager.flush();
                }
            }

            // 포장재(Packaging Materials) 저장 로직
            if (completeProductionPlan.getPackagingMaterials() != null && !completeProductionPlan.getPackagingMaterials().isEmpty()) {
                System.out.println("🔍 포장재 저장 시작");
                for (PlanMaterialDTO materialDTO : completeProductionPlan.getPackagingMaterials()) {
                    // 맥주 이름으로 제품 찾기
                    ProductionPlanDTO relatedProduct = completeProductionPlan.getProducts().stream()
                            .filter(p -> p.getProductName().equals(materialDTO.getBeerName()))
                            .findFirst()
                            .orElse(null);

                    if (relatedProduct == null) {
                        System.out.println("🚨 경고: " + materialDTO.getBeerName() + "에 해당하는 제품 정보 없음");
                        continue;
                    }

                    String productId = relatedProduct.getProductId();
                    String relatedPlanProductId = productPlanIdMap.get(productId);

                    if (relatedPlanProductId == null) {
                        System.out.println("🚨 경고: " + productId + "에 해당하는 PLAN_PRODUCT_ID 없음");
                        continue;
                    }

                    PlanMaterial planMaterial = new PlanMaterial();
                    planMaterial.setPlanMaterialId(generateNewPlanMaterialId());

                    PlanProduct planProduct = new PlanProduct();
                    planProduct.setPlanProductId(relatedPlanProductId);
                    planProduct.setProductId(productId);

                    planMaterial.setPlanProduct(planProduct);
                    planMaterial.setMaterialId(materialDTO.getMaterialId());
                    planMaterial.setMaterialName(materialDTO.getMaterialName());
                    planMaterial.setMaterialType(materialDTO.getMaterialType());
                    planMaterial.setUnit(materialDTO.getUnit());
                    planMaterial.setStdQty(materialDTO.getStdQty());
                    planMaterial.setPlanQty(materialDTO.getPlanQty());
                    planMaterial.setCurrentStock(materialDTO.getCurrentStock());
                    planMaterial.setBeerName(materialDTO.getBeerName());
                    planMaterial.setStatus(materialDTO.getStatus());

                    System.out.println("✅ 저장될 포장재 정보: " + planMaterial);

                    planMaterialRepository.save(planMaterial);
                    entityManager.flush();
                }
            }

            System.out.println("✅ 자재 저장 완료");
        } else {
            System.out.println("⚠️ 제품 정보가 null입니다.");
        }



        // ✅ Step 4: MATERIAL_REQUEST 저장 (자재 구매 신청)
        if (completeProductionPlan.getMaterialRequests() != null) {
            MaterialRequestDTO requestDTO = completeProductionPlan.getMaterialRequests();

            if (requestDTO.getMaterials() != null && !requestDTO.getMaterials().isEmpty()) {
                for (MaterialRequestDTO.MaterialRequestInfo materialRequestInfo : requestDTO.getMaterials()) {
                    // null 체크 추가
                    if (materialRequestInfo.getMaterialId() == null) {
                        System.out.println("🚨 경고: materialId가 null입니다.");
                        continue; // 다음 반복으로 건너뜀
                    }

                    // 모든 PlanProduct에 대해 반복하며 PlanMaterial 찾기
                    boolean materialFound = false;
                    for (String planProductId : productPlanIdMap.values()) {
                        Optional<PlanMaterial> planMaterialOpt = planMaterialRepository
                                .findByMaterialIdAndPlanProduct_PlanProductId(
                                        materialRequestInfo.getMaterialId(),
                                        planProductId
                                );

                        if (planMaterialOpt.isPresent()) {
                            PlanMaterial planMaterial = planMaterialOpt.get();

                            MaterialRequest materialRequest = new MaterialRequest();
                            materialRequest.setRequestId(generateNewMaterialRequestId());
                            materialRequest.setPlanMaterial(planMaterial);
                            materialRequest.setRequestQty(materialRequestInfo.getRequestQty());
                            materialRequest.setDeliveryDate(requestDTO.getDeliveryDate());
                            materialRequest.setReason(requestDTO.getReason());
                            materialRequest.setNote(requestDTO.getNote());

                            // 로깅 추가
                            System.out.println("🚀 자재 요청 정보 저장: " +
                                    "RequestId: " + materialRequest.getRequestId() +
                                    ", MaterialId: " + materialRequestInfo.getMaterialId() +
                                    ", RequestQty: " + materialRequest.getRequestQty() +
                                    ", PlanProductId: " + planProductId);

                            materialRequestRepository.save(materialRequest);
                            materialFound = true;
                            break;
                        }
                    }

                    if (!materialFound) {
                        System.out.println("🚨 경고: 해당 자재의 생산 계획 정보를 찾을 수 없습니다. MaterialId: " + materialRequestInfo.getMaterialId());
                    }
                }

                System.out.println("✅ 자재 요청 저장 완료");
            } else {
                System.out.println("⚠️ 자재 요청 정보가 비어있습니다.");
            }
        } else {
            System.out.println("⚠️ 자재 요청 정보가 null입니다.");
        }

        return planProductIds;
    }

    private String generateNewPlanMaterialId() {
        String maxId = planMaterialRepository.findMaxPlanMaterialId();
        System.out.println("🔍 현재 DB에서 가장 큰 PLAN_MATERIAL_ID: " + maxId);

        if (maxId == null) {
            System.out.println("🚀 첫 번째 PLAN_MATERIAL_ID 생성: PM00001");
            return "PM00001"; // 첫 번째 ID
        }

        try {
            int numericPart = Integer.parseInt(maxId.substring(2)); // "PM00005" -> 5
            numericPart++; // 6으로 증가
            String newId = String.format("PM%05d", numericPart); // "PM00006" 형식으로 변환
            System.out.println("🚀 새롭게 생성된 PLAN_MATERIAL_ID: " + newId);
            return newId;
        } catch (NumberFormatException e) {
            throw new RuntimeException("🚨 PLAN_MATERIAL_ID 생성 오류: " + maxId, e);
        }
    }


    @Transactional
    public void saveProductionLine(List<PlanLineDTO> lineDTOs, String planProductId) {
        List<PlanLine> planLines = lineDTOs.stream()
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
}