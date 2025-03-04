package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.work.model.repository.LineMaterialRepository;
import com.org.qualitycore.standardinformation.model.repository.MaterialGrindingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialGrindingService {

        private final MaterialGrindingRepository materialGrindingRepository;
        private final LineMaterialRepository lineMaterialRepository;
        private final ModelMapper modelMapper;




    // ✅ 작업지시 ID 목록 조회
    @Transactional
    public List<LineMaterialNDTO> getLineMaterial() {
        log.info("서비스: 작업지시 ID 목록 조회 시작");
        List<LineMaterial> lineMaterialList = lineMaterialRepository.findAllLineMaterial();
        log.info("서비스: 조회된 작업지시 ID 목록 {}", lineMaterialList);
        return lineMaterialList.stream()
                .map(material -> {
                    LineMaterialNDTO dto = modelMapper.map(material, LineMaterialNDTO.class);
                    dto.setLotNo(material.getWorkOrders() != null ? material.getWorkOrders().getLotNo() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // ✅ 특정 LOT_NO에 대한 자재 정보 조회
    @Transactional(readOnly = true)
    public List<LineMaterialNDTO> getMaterialsByLotNo(String lotNo) {
        log.info("서비스: LOT_NO={}에 대한 자재 정보 조회", lotNo);
        List<LineMaterial> materials = lineMaterialRepository.findByWorkOrders_LotNo(lotNo);
        return materials.stream()
                .map(material -> {
                    LineMaterialNDTO dto = modelMapper.map(material, LineMaterialNDTO.class);
                    dto.setLotNo(material.getWorkOrders() != null ? material.getWorkOrders().getLotNo() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }



    // 분쇄 공정 등록
        @Transactional
        public Message createMaterialGrinding(MaterialGrindingDTO materialGrindingDTO) {
            try {
                log.info("서비스 : 분쇄공정 등록 시작 DTO {}", materialGrindingDTO);

                // ID 자동 생성
                String generatedId = generateNextGrindingId();
                log.info("자동으로 생성되는 ID {}", generatedId);

                // ✅ 특정 LOT_NO에 대한 자재 정보 가져오기
                List<LineMaterial> lineMaterials = lineMaterialRepository.findByWorkOrders_LotNo(materialGrindingDTO.getLotNo());
                if (lineMaterials.isEmpty()) {
                    return new Message(HttpStatus.BAD_REQUEST.value(), "LOT_NO가 존재하지 않습니다.", null);
                }

                // ✅ ModelMapper 를 사용하여 DTO -> Entity 변환
                MaterialGrinding materialGrinding = modelMapper.map(materialGrindingDTO, MaterialGrinding.class);

                // ✅ ID 자동 생성
                materialGrinding.setGrindingId(generatedId);

                // ✅ 관련 엔티티 매핑 (LOT_NO 기반으로 LineMaterial 리스트 설정)
                materialGrinding.setLineMaterials(lineMaterials);

                // ✅ 기본값 설정
                if (materialGrinding.getProcessStatus() == null) {
                    materialGrinding.setProcessStatus("대기중");
                }
                if (materialGrinding.getStatusCode() == null) {
                    materialGrinding.setStatusCode("SC001");
                }

                // ✅ 시작 시간 설정 (DTO 값이 있으면 사용, 없으면 현재 시간)
                if (materialGrinding.getStartTime() == null) {
                    materialGrinding.setStartTime(LocalDateTime.now());
                }

                // ✅ 예상 종료 시간 자동 계산
                if (materialGrinding.getExpectedEndTime() == null && materialGrinding.getGrindDuration() != null) {
                    materialGrinding.setExpectedEndTime(materialGrinding.getStartTime().plusMinutes(materialGrinding.getGrindDuration()));
                }

                log.info("ModelMapper 변환 완료 !! {}", materialGrinding);

                // ✅  DB 저장
                MaterialGrinding savedMaterialGrinding = materialGrindingRepository.save(materialGrinding);
                log.info("서비스 분쇄 공정 등록 완료 ! {}", savedMaterialGrinding);


                // ✅ DTO 변환 후 반환
                MaterialGrindingDTO responseDTO = modelMapper.map(savedMaterialGrinding, MaterialGrindingDTO.class);
                Map<String, Object> result = new HashMap<>();
                result.put("savedMaterialGrinding", responseDTO);
                return new Message(HttpStatus.CREATED.value(), "분쇄공정 등록 완료!", result);


            } catch(IllegalArgumentException e){
                log.error("서비스 : 입력값 오류 발생 - 이유: {}", e.getMessage(), e);
                return new Message(HttpStatus.BAD_REQUEST.value(), "입력값 오류: " + e.getMessage(),new HashMap<>());

            } catch(Exception e) {
                log.error("서비스 : 분쇄공정 등록중 오류 발생 {}", e.getMessage(), e);
                return new Message(HttpStatus.BAD_REQUEST.value(), "분쇄 공정 등록 실패" + e.getMessage(),new HashMap<>());
            }
        }


        // 가장 큰 "grindingId" 조회 후 다음 ID 생성 하룻 있는 코드!
        public String generateNextGrindingId(){
            Integer maxId = materialGrindingRepository.findMaxGrindingId();
            int nextId = (maxId != null) ? maxId + 1 : 1;
            return String.format("GR%03d", nextId); // "GR001"형식!

        }


        // 실제 종료시간 업데이트
        public MaterialGrindingDTO completeEndTime(String grindingId) {
            MaterialGrinding materialGrinding = materialGrindingRepository.findById(grindingId)
                .orElseThrow(() -> new RuntimeException("분쇄 ID가 존재하지 않습니다."));
            materialGrinding.setActualEndTime(LocalDateTime.now());
            MaterialGrinding updatedGrinding = materialGrindingRepository.save(materialGrinding);
            return modelMapper.map(updatedGrinding, MaterialGrindingDTO.class);
        }


    // 🔹 공정 시작 (대기중 → 진행중)
    public MaterialGrindingDTO startGrindingProcess(String grindingId) {
        MaterialGrinding materialGrinding = materialGrindingRepository.findById(grindingId)
                .orElseThrow(() -> new RuntimeException("❌ 분쇄 ID가 존재하지 않습니다."));

        materialGrinding.setProcessStatus("진행중");
        materialGrinding.setStartTime(LocalDateTime.now());
        materialGrinding.setExpectedEndTime(materialGrinding.getStartTime().plusMinutes(materialGrinding.getGrindDuration()));

        MaterialGrinding updatedGrinding = materialGrindingRepository.save(materialGrinding);
        return modelMapper.map(updatedGrinding, MaterialGrindingDTO.class);
    }

    // 🔹 공정 완료 (진행중 → 완료)
    public MaterialGrindingDTO completeGrindingProcess(String grindingId) {
        MaterialGrinding materialGrinding = materialGrindingRepository.findById(grindingId)
                .orElseThrow(() -> new RuntimeException("❌ 분쇄 ID가 존재하지 않습니다."));

        if (!"진행중".equals(materialGrinding.getProcessStatus())) {
            throw new RuntimeException("❌ 진행중 상태가 아닌 공정을 완료할 수 없습니다.");
        }

        materialGrinding.setProcessStatus("완료");
        materialGrinding.setActualEndTime(LocalDateTime.now());

        MaterialGrinding updatedGrinding = materialGrindingRepository.save(materialGrinding);
        return modelMapper.map(updatedGrinding, MaterialGrindingDTO.class);
    }
}





