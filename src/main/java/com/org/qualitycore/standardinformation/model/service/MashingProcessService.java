package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.repository.MashingProcessRepository;
import com.org.qualitycore.standardinformation.model.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MashingProcessService {

    private final MashingProcessRepository mashingProcessRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ModelMapper modelMapper;





}
