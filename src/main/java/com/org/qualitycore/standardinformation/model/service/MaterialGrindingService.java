package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.work.model.repository.LineMaterialRepository;
import com.org.qualitycore.standardinformation.model.repository.MaterialGrindingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialGrindingService {

        private final MaterialGrindingRepository materialGrindingRepository;
        private final LineMaterialRepository lineMaterialRepository;
        private final ModelMapper modelMapper;




    // ✅ 작업지시 ID 목록 조회
        @Transactional(readOnly = true)
        public List<LineMaterialNDTO> getLineMaterial() {
            log.info("서비스: 작업지시 ID 목록 조회 시작");
            List<LineMaterial> lineMaterialList = lineMaterialRepository.findAllLineMaterial();
            log.info("서비스: 조회된 작업지시 ID 목록 {}", lineMaterialList);
            return lineMaterialList.stream()
                    .map(material -> modelMapper.map(material, LineMaterialNDTO.class))
                    .collect(Collectors.toList());
        }


        // ✅ 특정 LOT_NO에 대한 자재 정보 조회
        @Transactional(readOnly = true)
        public List<LineMaterialNDTO> getMaterialsByLotNo(String lotNo) {
            log.info("서비스: LOT_NO={}에 대한 자재 정보 조회", lotNo);
            List<LineMaterial> materials = lineMaterialRepository.findByLotNo(lotNo);

            return materials.stream()
                    .map(material -> modelMapper.map(material, LineMaterialNDTO.class))
                    .collect(Collectors.toList());
        }





        // 분쇄 공정 등록
        @Transactional
        public ErpMessage createMaterialGrinding(MaterialGrindingDTO materialGrindingDTO) {
            try {
                log.info("서비스 : 분쇄공정 등록 시작 DTO {}", materialGrindingDTO);

                // ID 자동 생성
                String generatedId = generateNextGrindingId();
                log.info("자동으로 생성되는 ID {}", generatedId);


                // ✅ 특정 LOT_NO에 대한 자재 정보 가져오기
                List<LineMaterial> lineMaterials =lineMaterialRepository.findByLotNo(materialGrindingDTO.getLotNo());
                if (lineMaterials.isEmpty()) {
                    return new ErpMessage(HttpStatus.BAD_REQUEST.value(), "LOT_NO가 존재하지 않습니다.");
                }

                // ✅ 첫 번째 엔티티 선택 (또는 특정 기준으로 필터링 가능)
                LineMaterial selectedLineMaterial = lineMaterials.get(0);

                MaterialGrinding materialGrinding = new MaterialGrinding();
                materialGrinding.setGrindingId(generatedId);
                materialGrinding.setLineMaterials(lineMaterials);
                materialGrinding.setProcessStatus("대기중");
                materialGrinding.setStartTime(LocalDateTime.now());


                // 기본값 설정
                if (materialGrinding.getProcessStatus() == null) {
                    materialGrinding.setProcessStatus("대기중");
                }
                if (materialGrinding.getStatusCode() == null) {
                    materialGrinding.setStatusCode("SC001");
                }

                // ✅ 시작 시간이 null이면 현재 시간으로 설정 (DTO에서 값을 못 가져올 경우 대비)
                if (materialGrindingDTO.getStartTime() != null) {
                    materialGrinding.setStartTime(materialGrindingDTO.getStartTime());
                } else {
                    materialGrinding.setStartTime(LocalDateTime.now());
                }

                // ✅ expectedEndTime 자동 설정
                if (materialGrinding.getExpectedEndTime() == null && materialGrinding.getGrindDuration() != null) {
                    materialGrinding.setExpectedEndTime(materialGrinding.getStartTime().plusMinutes(materialGrinding.getGrindDuration()));
                }

                log.info("엔티티 변환 완료 !! {}", materialGrinding);

                // DB 저장
                MaterialGrinding savedMaterialGrinding = materialGrindingRepository.save(materialGrinding);
                log.info("서비스 분쇄 공정 등록 완료 ! {}", savedMaterialGrinding);
                return new ErpMessage(HttpStatus.CREATED.value(), "분쇄공정 등록 완료!");


              // 필수 값이 빠졌거나 존재하지 않는 값을 입력할경우 예외 발생!
            } catch(IllegalArgumentException e){
                log.error("서비스 : 입력값 오류 발생 - 이유: {}", e.getMessage(), e);
                return new ErpMessage(HttpStatus.BAD_REQUEST.value(), "입력값 오류: " + e.getMessage());

             //알수 없는 예외 오류시 응답 반환!
            }catch(Exception e) {
                log.error("서비스 : 분쇄공정 등록중 오류 발생 {}" ,e.getMessage(),e);
                return new ErpMessage(HttpStatus.BAD_REQUEST.value(), "분쇄 공정 등록 실패" + e.getMessage());
            }
        }


        // 가장 큰 "grindingId" 조회 후 다음 ID 생성 하룻 있는 코드!
        public String generateNextGrindingId(){
            Integer maxId = materialGrindingRepository.findMaxGrindingId();
            int nextId = (maxId != null) ? maxId + 1 : 1;
            return String.format("GR%03d", nextId); // "GR001"형식!

        }


        // 실제 종료시간 업데이트
        public MaterialGrindingDTO completeGrindingProcess(String grindingId) {
            MaterialGrinding materialGrinding = materialGrindingRepository.findById(grindingId)
                .orElseThrow(() -> new RuntimeException("분쇄 ID가 존재하지 않습니다."));
            materialGrinding.setActualEndTime(LocalDateTime.now());
            MaterialGrinding updatedGrinding = materialGrindingRepository.save(materialGrinding);
            return modelMapper.map(updatedGrinding, MaterialGrindingDTO.class);
        }



}
