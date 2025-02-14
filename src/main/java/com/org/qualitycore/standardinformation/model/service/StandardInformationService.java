package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.WorkplaceDTO;
import com.org.qualitycore.standardinformation.model.entity.Workplace;
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
    private final ModelMapper modelMapper;

    // 작업장 전체 조회
    public List<Workplace> getAllWorkplaces() {
        return workplaceRepository.findAll();
    }

    //작업장 등록
    public Workplace createWorkplace(WorkplaceDTO workplaceDTO) {
        Workplace workplace = Workplace.builder()
                .workplaceId(workplaceDTO.getWorkplaceId())
                .workplaceName(workplaceDTO.getWorkplaceName())
                .workplaceCode(workplaceDTO.getWorkplaceCode())
                .workplaceLocation(workplaceDTO.getWorkplaceLocation())
                .workplaceType(workplaceDTO.getWorkplaceType())
                .lineId(workplaceDTO.getLineId())
                .workplaceStatus(workplaceDTO.getWorkplaceStatus())
                .managerName(workplaceDTO.getManagerName())
                .workplaceCapacity(workplaceDTO.getWorkplaceCapacity())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return workplaceRepository.save(workplace);
    }

    // 작업장 등록 수정하기
    public Workplace updateWorkplace(String id, WorkplaceDTO workplaceDTO) {
        Workplace workplace = workplaceRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 작업장은 존재하지 않아요! ID:" +id));
        // DTO 받은 값이 null 이 아니면 업데이드 ㄱ
        Workplace updateWorkplace = workplace.toBuilder()
                .workplaceId(workplaceDTO.getWorkplaceId() !=null ? workplaceDTO.getWorkplaceId() : workplace.getWorkplaceId())
                .lineId(workplaceDTO.getLineId() !=null ? workplaceDTO.getLineId() : workplace.getLineId())
                .workplaceName(workplaceDTO.getWorkplaceName() !=null ? workplaceDTO.getWorkplaceName() : workplace.getWorkplaceName())
                .workplaceType(workplaceDTO.getWorkplaceType() !=null ? workplaceDTO.getWorkplaceType() : workplace.getWorkplaceType())
                .workplaceStatus(workplaceDTO.getWorkplaceStatus() !=null ? workplaceDTO.getWorkplaceStatus() : workplace.getWorkplaceStatus())
                .workplaceLocation(workplaceDTO.getWorkplaceLocation() !=null ? workplaceDTO.getWorkplaceLocation() : workplace.getWorkplaceLocation())
                .managerName(workplaceDTO.getManagerName() !=null ? workplaceDTO.getManagerName() : workplace.getManagerName())
                .workplaceCapacity(workplaceDTO.getWorkplaceCapacity() !=null ? workplaceDTO.getWorkplaceCapacity() : workplace.getWorkplaceCapacity() )
                .build();
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
