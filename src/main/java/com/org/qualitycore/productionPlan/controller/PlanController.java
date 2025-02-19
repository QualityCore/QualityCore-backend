package com.org.qualitycore.productionPlan.controller;

import com.org.qualitycore.productionPlan.model.dto.PlanLineDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductBomDTO;
import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
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
    public void createProductionPlan(@RequestBody ProductionPlanDTO dto) {
         planService.saveProductionPlan(dto);
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

//    // 특정 제품의 생산라인 배정 조회
//    @GetMapping("/plans/lines/{planProductId}")
//    public ResponseEntity<List<PlanLineDTO>> getProductionLines(@PathVariable String planProductId) {
//        List<PlanLineDTO> planLines = planService.getProductionLines(planProductId);
//        return ResponseEntity.ok(planLines);
//    }
//
//    // 생산라인 배정 등록
//    @PostMapping("lines")
//    public ResponseEntity<String> createProductionLine(@RequestBody List<PlanLineDTO> planLineDTOs) {
//        planService.saveProductionLines(planLineDTOs);
//        return ResponseEntity.ok("생산라인 배정이 완료되었습니다.");
//    }
}