package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.standardinformation.model.entity.StandardInformationMessage;
import com.org.qualitycore.standardinformation.model.entity.WorkOrder;
import com.org.qualitycore.standardinformation.model.repository.ProductionProcessRepository;
import com.org.qualitycore.standardinformation.model.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionProcessService {

    private final ProductionProcessRepository productionProcessRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ModelMapper modelMapper;


    public StandardInformationMessage createMaterialGrinding(MaterialGrindingDTO materialGrindingDTO) {
        try{
            log.info("서비스 : 분쇄공정 등록 시작 DTO {}" , materialGrindingDTO);

            // ID 자동 생성
            String generatedId = generateNextWorkplaceId();
            materialGrindingDTO.setGrindingId(generatedId);
            log.info("자동으로 생성되는 ID {}",generatedId);

            WorkOrder workOrder=
                    workOrderRepository.findById(materialGrindingDTO.getLotNo())
                            .orElseThrow(()->new IllegalArgumentException(
                                    "존재하지 않는 LOT_NO 입니다." + materialGrindingDTO.getLotNo()));


            //DTO 에서 엔티티로 변환
            MaterialGrinding materialGrinding = modelMapper.map(materialGrindingDTO,MaterialGrinding.class);

            // FK 설정
            materialGrinding.set

        }
    }
}

