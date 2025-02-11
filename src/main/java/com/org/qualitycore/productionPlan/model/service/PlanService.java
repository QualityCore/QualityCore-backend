package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.productionPlan.model.entity.ProductionPlan;
import com.org.qualitycore.productionPlan.model.repository.PlanRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final ModelMapper modelMapper;

    public List<ProductionPlan> getAllProductionPlans(String planYm, String status, Long productId) {
        return planRepository.findAll(); // 나중에 필터 추가 가능
    }
}
