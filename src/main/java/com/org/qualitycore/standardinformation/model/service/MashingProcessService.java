package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.dto.ProcessTrackingDTONam;
import com.org.qualitycore.work.model.entity.WorkOrders;
import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.dto.MashingProcessDTO;
import com.org.qualitycore.standardinformation.model.entity.MashingProcess;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.repository.MashingProcessRepository;
import com.org.qualitycore.work.model.entity.processTracking;
import com.org.qualitycore.work.model.repository.LineMaterialRepository;
import com.org.qualitycore.work.model.repository.ProcessTrackingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Slf4j
public class MashingProcessService {

    private final MashingProcessRepository mashingProcessRepository;
    private final LineMaterialRepository lineMaterialRepository;
    private final ProcessTrackingRepository processTrackingRepository;
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
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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




    // ✅ 당화 공정 등록
    @Transactional
    public Message createMashingProcess(MashingProcessDTO mashingProcessDTO) {
        try {
            log.info("서비스 : 당화공정 등록 시작 DTO {}", mashingProcessDTO);

            // DTO 가 null 인지 체크
            if(mashingProcessDTO == null){
                return new Message(HttpStatus.BAD_REQUEST.value(),
                        "MashingProcessDTO 가 null 임", new HashMap<>());
            }

            // ID 자동 생성
            String generatedId = generateNextMashingId();
            log.info("자동으로 생성되는 ID {}", generatedId);


            // ✅ 특정 LOT_NO에 대한 자재 정보 가져오기
            List<LineMaterial> lineMaterials = lineMaterialRepository.
                    findByWorkOrders_LotNo(mashingProcessDTO.getLotNo());
            if (lineMaterials.isEmpty()) {
                return new Message(HttpStatus.BAD_REQUEST.value(),
                        "LOT_NO가 존재하지 않습니다.", new HashMap<>());
            }


            // ✅ ModelMapper 를 사용하여 DTO -> Entity 변환
            MashingProcess mashingProcess = modelMapper
                    .map(mashingProcessDTO, MashingProcess.class);

            // ✅ ID 자동 생성
            mashingProcess.setMashingId(generatedId);

            // ✅ 관련 엔티티 매핑 (LOT_NO 기반으로 LineMaterial 리스트 설정)
            mashingProcess.setLineMaterials(lineMaterials);

            // ✅ WorkOrders 가져오기
            WorkOrders workOrders = lineMaterials.get(0).getWorkOrders();

           // LOT_NO를 기반으로 기존 ProcessTracking 조회
            processTracking processTrackingA =processTrackingRepository
                    .findByLotNo(mashingProcessDTO.getLotNo());
            if(processTrackingA == null){
                processTrackingA = new processTracking();
            }

            // ✅ `processTracking`에 `WorkOrders` 설정
            processTrackingA.setWorkOrders(workOrders); // ✅ LOT_NO와 연결

            // ✅ ProcessTracking 에 lotNo를 직접 설정할 수 없으므로, WorkOrders 에서 가져와 사용
            processTrackingA.setStatusCode("SC002");
            processTrackingA.setProcessStatus("대기 중");
            processTrackingA.setProcessName("당화");

            // ✅ ProcessTracking 저장
            processTrackingA = processTrackingRepository.save(processTrackingA);

            // ✅ `processTracking`을 `mashingProcess`에 설정
            mashingProcess.setProcessTracking(processTrackingA);

            // ✅ 시작 시간 설정 (DTO 값이 있으면 사용, 없으면 현재 시간)
            if (mashingProcess.getStartTime() == null) {
                mashingProcess.setStartTime(LocalDateTime.now());
            }

            // ✅ 예상 종료 시간 자동 계산
            if (mashingProcess.getExpectedEndTime() == null && mashingProcess.getMashingTime() != null) {
                mashingProcess.setExpectedEndTime(mashingProcess.getStartTime().plusMinutes(mashingProcess.getMashingTime()));
            }

            log.info("ModelMapper 변환 완료 !! {}", mashingProcess);

            // ✅ DB 저장
            MashingProcess savedMashingProcess = mashingProcessRepository.save(mashingProcess);
            log.info("서비스 당화 공정 등록 완료 ! {}", savedMashingProcess);

            // ✅ DTO 변환 후 반환
            MashingProcessDTO responseDTO = modelMapper.map(savedMashingProcess, MashingProcessDTO.class);

            // ✅ lotNo가 누락되지 않도록 직접 설정
            if (savedMashingProcess.getProcessTracking() != null
                    && savedMashingProcess.getProcessTracking().getWorkOrders() != null) {
                responseDTO.getProcessTracking().setLotNo(
                        savedMashingProcess.getProcessTracking().getWorkOrders().getLotNo()
                );
            }

            Map<String, Object> result = new HashMap<>();
            result.put("savedMashingProcess", responseDTO);
            return new Message(HttpStatus.CREATED.value(), "분쇄공정 등록 완료!", result);


        } catch(IllegalArgumentException e){
            log.error("서비스 : 입력값 오류 발생 - 이유: {}", e.getMessage(), e);
            return new Message(HttpStatus.BAD_REQUEST.value(), "입력값 오류: " + e.getMessage(), new HashMap<>());

        } catch(Exception e) {
            log.error("서비스 : 당화공정 등록중 오류 발생 {}", e.getMessage(), e);
            return new Message(HttpStatus.BAD_REQUEST.value(), "당화 공정 등록 실패: " + e.getMessage(), new HashMap<>());
        }
    }


    // 가장 큰 "grindingId" 조회 후 다음 ID 생성 하룻 있는 코드!
    public String generateNextMashingId(){
        Integer maxId = mashingProcessRepository.findMaxMashingId();
        int nextId = (maxId != null) ? maxId + 1 : 1;
        return String.format("MA%03d", nextId); // "MA001"형식!
    }


    // 실제 종료 시간 업데이트
    public Message completeMashingProcess(String mashingId , Double phValue) {
        MashingProcess mashingProcess = mashingProcessRepository.findById(mashingId)
                .orElseThrow(() -> new RuntimeException("분쇄 ID가 존재하지 않습니다."));

        // pH 값을 업데이트
        if(phValue != null) {
            mashingProcess.setPhValue(phValue);
        }

        mashingProcess.setActualEndTime(LocalDateTime.now());
        MashingProcess updatedMashing = mashingProcessRepository.save(mashingProcess);

        Map<String, Object> result = new HashMap<>();
        result.put("updatedMashing", modelMapper.map(updatedMashing, MashingProcessDTO.class));

        return new Message(HttpStatus.OK.value(), "당화 공정 완료", result);
    }



    @Transactional
    public Message updateMashingProcess(MashingProcessDTO mashingProcessDTO) {
        try {
            log.info("서비스 : 분쇄공정 업데이트 시작 DTO {}", mashingProcessDTO);

            // ✅ LOT_NO를 기반으로 기존 ProcessTracking 조회
            processTracking processTracking =
                    processTrackingRepository.findByLotNo(mashingProcessDTO.getLotNo());


            // ✅ DTO 가 null 인지 체크
            if (mashingProcessDTO == null || mashingProcessDTO.getLotNo() == null) {
                return new Message(HttpStatus.BAD_REQUEST.value(),
                        "mashingProcessDTO 또는 LOT_NO가 null 입니다.", new HashMap<>());
            }


            // ✅ trackingId가 없으면 업데이트 불가
            if (processTracking.getTrackingId() == null) {
                return new Message(HttpStatus.BAD_REQUEST.value(),
                        "ProcessTracking 의 ID가 없습니다.", new HashMap<>());
            }


            // ✅ DTO 에서 ProcessTracking 정보를 가져와서 업데이트
            if (mashingProcessDTO.getProcessTracking() != null) {
                ProcessTrackingDTONam trackingDTO = mashingProcessDTO.getProcessTracking();

                if (trackingDTO.getStatusCode() != null) {
                    processTracking.setStatusCode(trackingDTO.getStatusCode());
                }

                if (trackingDTO.getProcessStatus() != null) {
                    processTracking.setProcessStatus(trackingDTO.getProcessStatus());
                }

                if (trackingDTO.getProcessName() != null) {
                    processTracking.setProcessName(trackingDTO.getProcessName());
                }
                log.info("DTO 에서 받은 값: StatusCode={}, ProcessStatus={}, ProcessName={}",
                        trackingDTO.getStatusCode(), trackingDTO.getProcessStatus(), trackingDTO.getProcessName());

            }

            log.info("업데이트된 ProcessTracking: {}", processTracking);


            // ✅ 기존 데이터를 업데이트 (UPDATE 수행)
            processTrackingRepository.save(processTracking);


            // ✅ Hibernate Proxy 를 제거한 DTO 변환 후 반환
            ProcessTrackingDTONam responseDTO = modelMapper.map(processTracking, ProcessTrackingDTONam.class);
            return new Message(HttpStatus.OK.value(),
                    "공정 상태 업데이트 완료!", Map.of("updatedProcessTracking", responseDTO));

        } catch (Exception e) {
            log.error("서비스 : 공정 상태 업데이트 중 오류 발생 {}", e.getMessage(), e);
            return new Message(HttpStatus.BAD_REQUEST.value(),
                    "공정 상태 업데이트 실패: " + e.getMessage(), new HashMap<>());
        }
    }


}
