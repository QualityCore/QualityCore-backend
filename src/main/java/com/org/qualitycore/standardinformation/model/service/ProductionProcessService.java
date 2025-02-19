package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.standardinformation.model.entity.WorkOrder;
import com.org.qualitycore.standardinformation.model.repository.ProductionProcessRepository;
import com.org.qualitycore.standardinformation.model.repository.WorkOrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductionProcessService {

    private final ProductionProcessRepository productionProcessRepository;
    private final WorkOrderRepository workOrderRepository;
    private ModelMapper modelMapper;

    // 작업 등록
    public MaterialGrindingDTO createMaterialGrinding(MaterialGrindingDTO dto) {
        MaterialGrinding materialGrinding = modelMapper.map(dto, MaterialGrinding.class);
        materialGrinding.setGrindingId(generateNextGrindingId());

        // ✅ Setter 사용하여 시작 시간 설정
        materialGrinding.setStartTime(LocalDateTime.now());

        materialGrinding.setExpectedEndTime(materialGrinding.getStartTime().plusMinutes(dto.getGrindDuration()));

        WorkOrder workOrder = workOrderRepository.findById(dto.getLotNo())
                .orElseThrow(() -> new RuntimeException("작업지시 ID가 존재하지 않습니다."));
        materialGrinding.setWorkOrder(workOrder);

        MaterialGrinding savedGrinding = productionProcessRepository.save(materialGrinding);
        return modelMapper.map(savedGrinding, MaterialGrindingDTO.class);
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

