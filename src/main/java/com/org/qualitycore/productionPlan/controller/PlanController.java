package com.org.qualitycore.productionPlan.controller;

import com.org.qualitycore.productionPlan.model.dto.ProductionPlanDTO;
import com.org.qualitycore.productionPlan.model.entity.ProductionPlan;
import com.org.qualitycore.productionPlan.model.repository.PlanProductRepository;
import com.org.qualitycore.productionPlan.model.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;



    @GetMapping("/api/v1/plan-overview")
    public List<ProductionPlanDTO> findProductionPlans(
            @RequestParam @DateTimeFormat(pattern = "yyyyMM") LocalDate planYm,
            @RequestParam(required = false) String status

    ){
        return planService.getAllProductionPlans(planYm, status);
    }


}
