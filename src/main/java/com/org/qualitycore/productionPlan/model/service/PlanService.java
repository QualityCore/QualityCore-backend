package com.org.qualitycore.productionPlan.model.service;

import com.org.qualitycore.productionPlan.model.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final ModelMapper modelMapper;
    private final PlanRepository planRepository;
}
