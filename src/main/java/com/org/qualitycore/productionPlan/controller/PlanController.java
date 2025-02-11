package com.org.qualitycore.productionPlan.controller;

import com.org.qualitycore.productionPlan.model.dto.PlanDTO;
import com.org.qualitycore.productionPlan.model.entity.ProductionPlan;
import com.org.qualitycore.productionPlan.model.repository.PlanRepository;
import com.org.qualitycore.productionPlan.model.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final PlanRepository planRepository;
    private final ModelMapper modelMapper;


    @GetMapping("/find")
    public List<PlanDTO> getAllProductionPlans(
            @RequestParam(required = false) String planYm,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long productId
    ){
        List<ProductionPlan> plans = planService.getAllProductionPlans(planYm, status, productId);
        return plans.stream()
                .map(plan -> modelMapper.map(plan,PlanDTO.class))
                .collect(Collectors.toList());
    }
}
