package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.standardinformation.model.entity.StandardInformationMessage;
import com.org.qualitycore.standardinformation.model.entity.WorkOrder;
import com.org.qualitycore.standardinformation.model.repository.ProductionProcessRepository;
import com.org.qualitycore.standardinformation.model.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductionProcessService {

    private final ProductionProcessRepository productionProcessRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ModelMapper modelMapper;

    // 분쇄 공정 등록
    public StandardInformationMessage createMaterialGrinding(MaterialGrindingDTO materialGrindingDTO) {
        System.out.println("서비스 DTO 데이터 확인!" + materialGrindingDTO);

        MaterialGrinding materialGrinding = modelMapper.map(materialGrindingDTO, MaterialGrinding.class);

        System.out.println("서비스 엔티티 반환 결과!" + materialGrinding);

        materialGrinding.setGrindingId(generateNextGrindingId());
        materialGrinding.setStartTime(LocalDateTime.now());
        materialGrinding.setExpectedEndTime(materialGrinding.getStartTime().plusMinutes(materialGrindingDTO.getGrindDuration()));

        System.out.println("ID 및 시간 설정 후 " + materialGrinding);

        WorkOrder workOrder = workOrderRepository.findById(materialGrindingDTO.getLotNo())
                .orElseThrow(() -> new RuntimeException("작업지시 ID가 존재하지 않습니다."));
        materialGrinding.setWorkOrder(workOrder);

        System.out.println("작업지시 설정 후!!" + materialGrinding);


        MaterialGrinding savedGrinding = productionProcessRepository.save(materialGrinding);

        System.out.println("저장 완료!!" + savedGrinding);

        return new StandardInformationMessage(HttpStatus.CREATED.value(),"분쇄공정 등록 완료");
    }


    // 실제 종료 시간 업데이트
    public MaterialGrindingDTO completeGrindingProcess(String grindingId) {
        MaterialGrinding materialGrinding = productionProcessRepository.findById(grindingId)
                .orElseThrow(() -> new RuntimeException("분쇄 ID가 존재하지 않습니다."));
        materialGrinding.setActualEndTime(LocalDateTime.now());
        MaterialGrinding updatedGrinding = productionProcessRepository.save(materialGrinding);
        return modelMapper.map(updatedGrinding, MaterialGrindingDTO.class);
    }

    // ✅ 가장 큰 `grindingId` 조회 후 다음 ID 생성 (GR001, GR002 형식 유지)
    public String generateNextGrindingId() {
        Integer maxId = productionProcessRepository.findMaxGrindingId();
        int nextId = (maxId != null) ? maxId + 1 : 1;
        return String.format("GR%03d", nextId);  // "GR001" 형식
    }




}

