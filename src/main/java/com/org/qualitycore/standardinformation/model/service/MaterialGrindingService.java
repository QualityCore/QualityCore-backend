package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.work.model.entity.WorkOrders;
import com.org.qualitycore.work.model.repository.LineMaterialRepository;
import com.org.qualitycore.standardinformation.model.repository.MaterialGrindingRepository;
import com.org.qualitycore.work.model.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
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


                //DTO 에서 엔티티로 변환
                MaterialGrinding materialGrinding = modelMapper
                    .map(materialGrindingDTO, MaterialGrinding.class);
                // ID 수동 설정 ( 엔티티 변환 후)
                materialGrinding.setGrindingId(generatedId);

                // LOT_NO 가 존재하는지 확인
                LineMaterial lineMaterial =
                        lineMaterialRepository.findByLotNo(materialGrindingDTO.getLotNo())
                                .stream().findFirst()
                                .orElseThrow(() -> new IllegalArgumentException
                                    ("존재하지 않는 LOT_NO 입니다." + materialGrindingDTO.getLotNo()));


                // FK 설정
                materialGrinding.setLotNo(materialGrindingDTO.getLotNo());
                materialGrinding.setLineMaterial(lineMaterial);

                // 기본값 설정
                if (materialGrinding.getProcessStatus() == null) {
                    materialGrinding.setProcessStatus("대기중");
                }
                if (materialGrinding.getStatusCode() == null) {
                    materialGrinding.setStatusCode("SC001");
                }

                // 시작 시간이 null 이면 현재시간을 설정
                if(materialGrinding.getStartTime() == null){
                    materialGrinding.setStartTime(LocalDateTime.now());
                }

                // 소요시간이 null 이면 예외 발생
                if(materialGrinding.getGrindDuration()==null){
                    throw new IllegalArgumentException("분쇄 소요 시간이 입력되지 않았습니다.");
                }

                // 예상 종료 시간 자동 계산 (시작시간 + 소요시간)
                materialGrinding.setExpectedEndTime(materialGrinding.getStartTime()
                    .plusMinutes(materialGrinding.getGrindDuration()));
                log.info("엔티티 변환 완료 !! {}", materialGrinding);

                // DB 저장
                MaterialGrinding savedMaterialGrinding = materialGrindingRepository.save(materialGrinding);
                log.info("서비스 분쇄 공정 등록 완료 ! {}", savedMaterialGrinding);

                return new ErpMessage(HttpStatus.CREATED.value(), "분쇄공정 등록 완료!");

              // 필수 값이 빠졌거나 존재하지 않는 값을 입력할경우 예외 발생!
            } catch(IllegalArgumentException e){
                log.error("서비스 : 입력값 오류 발생 - 이유: {}" , e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());


            }catch(DataIntegrityViolationException e){
                log.info("서비스 : 데이터 무결성 오류 발생 !!! {} " , e.getMessage(),e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"데이터베이스 제약 조건 위반: 필수 입력값 누락 또는 중복된 값 입력");

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
