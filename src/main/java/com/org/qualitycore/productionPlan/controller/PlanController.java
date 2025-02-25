package com.org.qualitycore.productionPlan.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.productionPlan.model.dto.PlanLineDTO;
import com.org.qualitycore.productionPlan.model.dto.PlanMaterialDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductBomDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PlanController {

    /*
    * /api/v1/products get -> 제품 전체조회
    * /api/v1/products/1402 get -> 1번 제품 상세 조회
    * /api/v1/products/1 post -> 1번 제품 등록
    * /api/v1/products/1 put -> 1번 제품 수정
    * /api/v1/products/1 delete -> 1번 제품 수정
    * 메소드 오버로딩 => 메소드 시그니처에 따라서 다르게 동작하는 메소드를 생성할 수 있다.
    * rest-api readme.md 파일 정리본 참조해서 rest-api 작성규칙!!!
    * http-status-code : 200, 201, 204, 403, 404, 401, 400, 500
    * */


    private final PlanService planService;

    // ✅ 응답 헤더 기본 설정
    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));
        return headers;
    }

    // 생산계획조회
    @GetMapping("/plans")
    public List<ProductionPlanDTO> findProductionPlans(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") String planYm,
            @RequestParam(required = false, defaultValue = "") String status // 기본값 추가
    ) {
        // 요청 로그
        System.out.println("요청 받은 planYm: " + planYm);
        System.out.println("요청 받은 status: " + status);

        // '2025-02' → '2025-02-01' ~ '2025-02-28' 변환
        LocalDate startDate = LocalDate.parse(planYm + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 서비스 호출
        List<ProductionPlanDTO> result = planService.getAllProductionPlans(startDate, endDate, status);

        // 결과
        System.out.println("응답 데이터 개수: " + result.size());
        return result;
    }

    // 생산계획 step1
    @PostMapping("/plans/step1")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createProductionPlan(@RequestBody ProductionPlanDTO dto) {
        String planProductId = planService.saveProductionPlan(dto);
        return ResponseEntity.ok(planProductId);
    }

    //제품 선택시 BOM정보 불러오기
    @GetMapping("/productBom/{productId}")
    public ResponseEntity<ProductBomDTO> getProductStandard(@PathVariable String productId) {
        ProductBomDTO productBom = planService.getProductStandard(productId);
        return ResponseEntity.ok(productBom);
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductBomDTO>> getAllProducts(){
        List<ProductBomDTO> products = planService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 특정 제품의 생산라인 배정 조회
    @GetMapping("/plans/lines/{planProductId}")
    public ResponseEntity<Message> getProductionLines(@PathVariable String planProductId) {
        List<PlanLineDTO> planLines = planService.getProductionLines(planProductId);

        if (planLines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Message(404, "해당 제품의 생산 라인 배정 데이터가 없습니다.", Map.of()));
        }

        return ResponseEntity.ok(new Message(200, "생산 라인 배정 데이터 조회 성공", Map.of("planLines", planLines)));
    }

    // 생산라인 배정 등록
    @PostMapping("/lines")
    public ResponseEntity<Message> createProductionLine(@RequestBody List<PlanLineDTO> planLineDTOs) {
        if (planLineDTOs == null || planLineDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Message(400, "요청 데이터가 비어 있습니다.", Map.of()));
        }

        planService.saveProductionLines(planLineDTOs);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Message(201, "생산 라인 배정이 완료되었습니다.", Map.of()));
    }

    // Step3 실시간 자재 소요량 계산
    @PostMapping("/materials/calculate")
    public ResponseEntity<Message> calculateMaterials(
            @RequestBody ProductionPlanDTO productionPlanDTO
    ) {
        try {
            // 받은 데이터 로깅
            System.out.println("받은 제품 정보: " + productionPlanDTO);
            System.out.println("제품 목록: " + productionPlanDTO.getProducts());

            Map<String, Object> result = planService.calculateMaterialRequirements(productionPlanDTO);

            return ResponseEntity.ok(new Message(
                    200,
                    "자재 소요량 계산 완료",
                    result
            ));
        } catch (Exception e) {
            // 에러 로깅 추가
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message(
                            500,
                            "자재 소요량 계산 중 오류 발생: " + e.getMessage(),
                            Map.of()
                    ));
        }
    }

    // 최종 저장 (Step 1,2,3 모두)
    @PostMapping("/save")
    public ResponseEntity<Message> savePlanWithMaterials(@RequestBody ProductionPlanDTO completeProductionPlan) {
        try {
            System.out.println("🚀 [컨트롤러] 받은 요청 데이터: " + completeProductionPlan);

            // ✅ Step 3 데이터 확인 (자재 리스트)
            if (completeProductionPlan.getMaterials() == null) {
                System.out.println("❌ [컨트롤러] materials 데이터가 NULL입니다.");
            } else {
                System.out.println("🔍 [컨트롤러] 받은 materials 크기: " + completeProductionPlan.getMaterials().size());
                for (PlanMaterialDTO material : completeProductionPlan.getMaterials()) {
                    System.out.println("   - Material ID: " + material.getMaterialId() + ", Name: " + material.getMaterialName());
                }
            }

            // ✅ Step 4 데이터 확인 (자재 구매 신청)
            if (completeProductionPlan.getMaterialRequests() == null) {
                System.out.println("❌ [컨트롤러] materialRequests 데이터가 NULL입니다.");
            } else {
                System.out.println("🔍 [컨트롤러] 받은 materialRequests: " + completeProductionPlan.getMaterialRequests());
            }

            List<String> savedPlanIds = planService.saveCompletePlan(completeProductionPlan);

            return ResponseEntity.ok(new Message(201, "생산 계획이 성공적으로 저장되었습니다.", Map.of("planIds", savedPlanIds)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message(500, "생산 계획 저장 중 오류 발생: " + e.getMessage(), Map.of()));
        }
    }

}
