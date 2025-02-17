package com.org.qualitycore.productionPlan.controller;

import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping("/api/v1/plan-overview")
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

        // 결과 로그
        System.out.println("응답 데이터 개수: " + result.size());
        return result;
    }

//    @PostMapping("/api/v1/plan-create")
//    @ResponseStatus(HttpStatus.OK)
//    public void createProductionPlan(@RequestBody ProductionPlanDTO dto) {
//         planService.saveProductionPlan(dto);
//    }
}
