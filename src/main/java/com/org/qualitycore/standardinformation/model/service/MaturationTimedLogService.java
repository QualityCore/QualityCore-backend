package com.org.qualitycore.standardinformation.model.service;


import com.org.qualitycore.standardinformation.model.dto.FermentationTimedLogDTO;
import com.org.qualitycore.standardinformation.model.dto.MaturationTimedLogDTO;
import com.org.qualitycore.standardinformation.model.entity.FermentationDetails;
import com.org.qualitycore.standardinformation.model.entity.FermentationTimedLog;
import com.org.qualitycore.standardinformation.model.entity.MaturationDetails;
import com.org.qualitycore.standardinformation.model.entity.MaturationTimedLog;
import com.org.qualitycore.standardinformation.model.repository.MaturationDetailsRepository;
import com.org.qualitycore.standardinformation.model.repository.MaturationTimedLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MaturationTimedLogService {
    private final MaturationTimedLogRepository maturationTimedLogRepository;
    private final MaturationDetailsRepository maturationDetailsRepository;
    private final ModelMapper modelMapper;


    // ✅ 숙성 공정 ID 목록 조회 서비스
    public List<String> findAllMaturationIds() {
        log.info("서비스: 숙성 공정 ID 목록 조회");
        return maturationDetailsRepository.findMaturationIds();
    }



    // ✅ 숙성 시간별 등록
    public MaturationTimedLog logMaturationData(MaturationTimedLogDTO maturationTimedLogDTO) {
        log.info("서비스: 숙성 공정 로그 저장 - {}", maturationTimedLogDTO);

        // ✅ `fermentationId`를 기반으로 `FermentationDetails` 찾기
        MaturationDetails details = maturationDetailsRepository.findByMaturationId(maturationTimedLogDTO.getMaturationId())
                .orElseThrow(() -> new RuntimeException("숙성 공정을 찾을 수 없습니다: " + maturationTimedLogDTO.getMaturationId()));

        // ✅ ModelMapper 를 사용하여 DTO 를 엔티티로 변환
        MaturationTimedLog log = modelMapper.map(maturationTimedLogDTO, MaturationTimedLog.class);
        log.setMaturationDetails(details);
        log.setStartTime(LocalDateTime.now()); // 시작 시간 설정

        return maturationTimedLogRepository.save(log);
    }


    // 실제 종료시간 업데이트
    public MaturationTimedLogDTO completeEndTime(Long flogId) {
        MaturationTimedLog maturationTimedLog = maturationTimedLogRepository.findById(flogId)
                .orElseThrow(() -> new RuntimeException("숙성 시간별 등록  ID가 존재하지 않습니다."));
        maturationTimedLog.setActualEndTime(LocalDateTime.now());
        MaturationTimedLog updatedMaturationTimedLog = maturationTimedLogRepository.save(maturationTimedLog);
        return modelMapper.map(updatedMaturationTimedLog, MaturationTimedLogDTO.class);
    }
}
