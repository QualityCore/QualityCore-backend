package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.dto.MashingProcessDTO;;
import com.org.qualitycore.standardinformation.model.entity.MashingProcess;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.repository.MashingProcessRepository;
import com.org.qualitycore.work.model.repository.LineMaterialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MashingProcessService {

    private final MashingProcessRepository mashingProcessRepository;
    private final LineMaterialRepository lineMaterialRepository;
    private final ModelMapper modelMapper;


    // ✅ 작업지시 ID 목록 조회
    @Transactional
    public List<LineMaterialNDTO> getLineMaterial() {
        log.info("서비스: 작업지시 ID 목록 조회 시작");
        List<LineMaterial> lineMaterialList = lineMaterialRepository.findAllLineMaterial();
        log.info("서비스: 조회된 작업지시 ID 목록 {}", lineMaterialList);
        return lineMaterialList.stream()
                .map(material -> modelMapper.map(material, LineMaterialNDTO.class))
                .collect(Collectors.toList());
    }


    // ✅ 특정 LOT_NO에 대한 자재 정보 조회
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<LineMaterialNDTO> getMaterialsByLotNo(String lotNo) {
        log.info("서비스: LOT_NO={}에 대한 자재 정보 조회", lotNo);
        List<LineMaterial> materials = lineMaterialRepository.findByLotNo(lotNo);

        return materials.stream()
                .map(material -> modelMapper.map(material, LineMaterialNDTO.class))
                .collect(Collectors.toList());
    }




    // ✅ 당화 공정 등록
    @Transactional
    public Message createMashingProcess(MashingProcessDTO mashingProcessDTO) {
        try {
            log.info("서비스 : 당화공정 등록 시작 DTO {}", mashingProcessDTO);

            // ID 자동 생성
            String generatedId = generateNextMashingId();
            log.info("자동으로 생성되는 ID {}", generatedId);

            // ✅ 특정 LOT_NO에 대한 자재 정보 가져오기
            List<LineMaterial> lineMaterials = lineMaterialRepository.findByLotNo(mashingProcessDTO.getLotNo());
            if (lineMaterials.isEmpty()) {
                return new Message(HttpStatus.BAD_REQUEST.value(), "LOT_NO가 존재하지 않습니다.", new HashMap<>());
            }

            // ✅ ModelMapper 를 사용하여 DTO -> Entity 변환
            MashingProcess mashingProcess = modelMapper.map(mashingProcessDTO, MashingProcess.class);

            // ✅ ID 자동 생성
            mashingProcess.setMashingId(generatedId);

            // ✅ 관련 엔티티 매핑 (LOT_NO 기반으로 LineMaterial 리스트 설정)
            mashingProcess.setLineMaterials(lineMaterials);

            // ✅ 기본값 설정
            if (mashingProcess.getProcessStatus() == null) {
                mashingProcess.setProcessStatus("대기중");
            }
            if (mashingProcess.getStatusCode() == null) {
                mashingProcess.setStatusCode("SC002");
            }

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
            Map<String, Object> result = new HashMap<>();
            result.put("mashingProcess", responseDTO);
            return new Message(HttpStatus.CREATED.value(), "당화공정 등록 완료!", result);

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
}
