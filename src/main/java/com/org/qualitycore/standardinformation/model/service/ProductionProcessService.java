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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionProcessService {

        private final ProductionProcessRepository productionProcessRepository;
        private final WorkOrderRepository workOrderRepository;
        private final ModelMapper modelMapper;


        // 분쇄 공정 등록
        @Transactional
        public StandardInformationMessage createMaterialGrinding(MaterialGrindingDTO materialGrindingDTO) {
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
                WorkOrder workOrder =
                    workOrderRepository.findByLotNo(materialGrindingDTO.getLotNo())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "존재하지 않는 LOT_NO 입니다." + materialGrindingDTO.getLotNo()));


                // FK 설정
                materialGrinding.setLotNo(materialGrindingDTO.getLotNo());
                materialGrinding.setWorkOrder(workOrder);

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
                MaterialGrinding savedMaterialGrinding = productionProcessRepository.save(materialGrinding);
                log.info("서비스 분쇄 공정 등록 완료 ! {}", savedMaterialGrinding);

                return new StandardInformationMessage(HttpStatus.CREATED.value(), "분쇄공정 등록 완료!");

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
                return new StandardInformationMessage(HttpStatus.BAD_REQUEST.value(), "분쇄 공정 등록 실패" + e.getMessage());
            }
        }


        // 가장 큰 "grindingId" 조회 후 다음 ID 생성 하룻 있는 코드!
        public String generateNextGrindingId(){
            Integer maxId = productionProcessRepository.findMaxGrindingId();
            int nextId = (maxId != null) ? maxId + 1 : 1;
            return String.format("GR%03d", nextId); // "GR001"형식!

        }




        // 실제 종료시간 업데이트
        public MaterialGrindingDTO completeGrindingProcess(String grindingId) {
            MaterialGrinding materialGrinding = productionProcessRepository.findById(grindingId)
                .orElseThrow(() -> new RuntimeException("분쇄 ID가 존재하지 않습니다."));
            materialGrinding.setActualEndTime(LocalDateTime.now());
            MaterialGrinding updatedGrinding = productionProcessRepository.save(materialGrinding);
            return modelMapper.map(updatedGrinding, MaterialGrindingDTO.class);
        }


    }
