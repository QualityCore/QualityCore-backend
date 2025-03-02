package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.MashingProcessDTO;;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.standardinformation.model.entity.MashingProcess;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.repository.MashingProcessRepository;
import com.org.qualitycore.work.model.repository.LineMaterialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MashingProcessService {

    private final MashingProcessRepository mashingProcessRepository;
    private final LineMaterialRepository lineMaterialRepository;
    private final ModelMapper modelMapper;



    @Transactional
    public ErpMessage createMashingProcess(MashingProcessDTO mashingProcessDTO) {
        try {
            log.info("서비스 : 당화공정 등록 시작 DTO {}", mashingProcessDTO);

            // ID 자동 생성
            String generatedId = generateNextMashingId();
            log.info("자동으로 생성되는 ID {}", generatedId);

            // ✅ 특정 LOT_NO에 대한 자재 정보 가져오기
            List<LineMaterial> lineMaterials =lineMaterialRepository.findByLotNo(mashingProcessDTO.getLotNo());
            if (lineMaterials.isEmpty()) {
               return new ErpMessage(HttpStatus.BAD_REQUEST.value(), "LOT_NO가 존재하지 않습니다.");
            }

            // ✅ 첫 번째 엔티티 선택 (또는 특정 기준으로 필터링 가능)
             LineMaterial selectedLineMaterial = lineMaterials.get(0);

            MashingProcess mashingProcess = new MashingProcess();
            mashingProcess.setMashingId(generatedId);
            mashingProcess.setLineMaterials(lineMaterials);
            mashingProcess.setProcessStatus("대기중");
            mashingProcess.setStartTime(LocalDateTime.now());


            // 기본값 설정
            if (mashingProcess.getProcessStatus() == null) {
                mashingProcess.setProcessStatus("대기중");
            }
            if (mashingProcess.getStatusCode() == null) {
                mashingProcess.setStatusCode("SC002");
            }

            // 시작 시간이 null 이면 현재시간을 설정
            if(mashingProcess.getStartTime() == null){
                mashingProcess.setStartTime(LocalDateTime.now());
            }

            // 예상 종료 시간 자동 계산
            mashingProcess.setExpectedEndTime(mashingProcess.getStartTime()
                    .plusMinutes(mashingProcess.getMashingTime()));

            log.info("엔티티 변환 완료 !! {}", mashingProcess);

            // DB 저장
            MashingProcess savedMashingProcess = mashingProcessRepository.save(mashingProcess);
            log.info("서비스: 당화공정 등록 완료! {}", savedMashingProcess);

            return new ErpMessage(HttpStatus.CREATED.value(), "당화공정 등록 완료!");

        } catch (DataIntegrityViolationException e) {
            log.error("❌ 데이터 무결성 오류 발생: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "데이터베이스 제약 조건 위반: 필수 입력값 누락 또는 중복된 값 입력");
        } catch (Exception e) {
            log.error("❌ 당화공정 등록 중 오류 발생: {}", e.getMessage(), e);
            return new ErpMessage(HttpStatus.BAD_REQUEST.value(), "당화공정 등록 실패: " + e.getMessage());
        }
    }


    // 가장 큰 "grindingId" 조회 후 다음 ID 생성 하룻 있는 코드!
    public String generateNextMashingId(){
        Integer maxId = mashingProcessRepository.findMaxMashingId();
        int nextId = (maxId != null) ? maxId + 1 : 1;
        return String.format("MA%03d", nextId); // "MA001"형식!
    }


    // 실제 종료시간 업데이트
    public MashingProcessDTO completeMashingProcess(String mashingId , Double phValue) {
        MashingProcess mashingProcess = mashingProcessRepository.findById(mashingId)
                .orElseThrow(() -> new RuntimeException("분쇄 ID가 존재하지 않습니다."));
        // pH 값을 업데이트
        if(phValue != null) {
            mashingProcess.setPhValue(phValue);
        }

        mashingProcess.setActualEndTime(LocalDateTime.now());
        MashingProcess updatedMashing = mashingProcessRepository.save(mashingProcess);
        return modelMapper.map(updatedMashing, MashingProcessDTO.class);
    }
}
