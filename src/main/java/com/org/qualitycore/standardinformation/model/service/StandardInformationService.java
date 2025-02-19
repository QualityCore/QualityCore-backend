package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.WorkplaceDTO;
import com.org.qualitycore.standardinformation.model.entity.LineInformation;
import com.org.qualitycore.standardinformation.model.entity.Workplace;
import com.org.qualitycore.standardinformation.model.repository.LineInformationRepository;
import com.org.qualitycore.standardinformation.model.repository.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StandardInformationService {

    private final WorkplaceRepository workplaceRepository;
    private final LineInformationRepository lineInformationRepository;
    private final ModelMapper modelMapper;

    // 작업장 전체 조회
    public List<WorkplaceDTO> getAllWorkplaces() {
        List<Workplace> workplaces = workplaceRepository.findAll();
        return workplaces.stream()
                .map(workplace ->modelMapper.map(workplace,WorkplaceDTO.class)).toList();
    }

    //작업장 등록
    public Workplace createWorkplace(WorkplaceDTO workplaceDTO) {
        LineInformation lineInformation = lineInformationRepository.findByLineId(workplaceDTO.getLineId())
                .orElseThrow(() -> new IllegalArgumentException
                        ("존재하지 않는 LINE_ID 입니다: " + workplaceDTO.getLineId()));
        Workplace workplace= modelMapper.map(workplaceDTO,Workplace.class);
        workplace.setWorkplaceId(generateNextWorkplaceId());
        workplace.setLineInformation(lineInformation);

        return workplaceRepository.save(workplace);
    }

    // ✅ 가장 큰 `workplaceId` 조회 후 다음 ID 생성 (WO001, WO002 형식 유지)
    public String generateNextWorkplaceId() {
        Integer maxId = workplaceRepository.findMaxWorkplaceId();
        int nextId = (maxId != null) ? maxId + 1 : 1;
        return String.format("WO%03d", nextId);  // "WO001" 형식
    }


    // 작업장 등록 수정하기
    @Transactional
    public Workplace updateWorkplace (String id, WorkplaceDTO workplaceDTO) {
        Workplace updateWorkplace = workplaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업장은 존재하지 않습니다! ID: " + id));
        workplaceDTO.setWorkplaceId(updateWorkplace.getWorkplaceId());
        modelMapper.map(workplaceDTO, updateWorkplace);
        return workplaceRepository.save(updateWorkplace);
    }



    // 작업장 등록 삭제

    @Transactional
    public void deleteWorkplace(String id) {
        Workplace workplace = workplaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업장이 존재하지 않습니다. ID: " + id));

        workplaceRepository.delete(workplace);
    }
}
