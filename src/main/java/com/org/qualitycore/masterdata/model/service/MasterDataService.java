package com.org.qualitycore.masterdata.model.service;

import com.org.qualitycore.masterdata.model.dto.WorkplaceDTO;
import com.org.qualitycore.masterdata.model.entity.Workplace;
import com.org.qualitycore.masterdata.model.repository.WorkplaceScheduleRepository;
import com.org.qualitycore.masterdata.model.repository.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final WorkplaceRepository workplaceRepository;
    private final WorkplaceScheduleRepository  workplaceScheduleRepository; //자식테이블 레파지토리 추가
    private final ModelMapper modelMapper;

    // 작업장 전체 조회
    public List<Workplace> getAllWorkplaces() {
        return workplaceRepository.findAll();
    }

    //작업장 등록
    public Workplace creactWorkplace(WorkplaceDTO workplaceDTO) {
        Workplace workplace = modelMapper.map(workplaceDTO,Workplace.class); // DTO->엔티티변환
        return workplaceRepository.save(workplace);
    }

    // 작업장 등록 수정하기
    public Workplace updateWorkplace(int id, WorkplaceDTO workplaceDTO) {
        Workplace workplace = workplaceRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 작업장은 존재하지 않아요! ID:" +id));
        // DTO 받은 값이 null 이 아니면 업데이드 ㄱ
        Workplace updateWorkplace = workplace.toBuilder()
                .workplaceName(workplaceDTO.getWorkplaceName() !=null ? workplaceDTO.getWorkplaceName() : workplace.getManagerName())
                .workplaceType(workplaceDTO.getWorkplaceType() !=null ? workplaceDTO.getWorkplaceType() : workplace.getWorkplaceType())
                .workplaceStatus(workplaceDTO.getWorkplaceStatus() !=null ? workplaceDTO.getWorkplaceStatus() : workplace.getWorkplaceStatus())
                .workplaceLocation(workplaceDTO.getWorkplaceLocation() !=null ? workplaceDTO.getWorkplaceLocation() : workplace.getWorkplaceLocation())
                .managerName(workplaceDTO.getManagerName() !=null ? workplaceDTO.getManagerName() : workplace.getManagerName())
                .workplaceCapacity(workplaceDTO.getWorkplaceCapacity() !=0 ? workplaceDTO.getWorkplaceCapacity() : workplace.getWorkplaceCapacity() )
                .build();
        return workplaceRepository.save(updateWorkplace);
    }

    // 작업장 등록 삭제하기
    @Transactional
    public void deleteWorkplace(String workplaceCode) {
        Workplace workplace = workplaceRepository.findByWorkplaceCode((workplaceCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업장이 존재하지 않습니다. Code: " + workplaceCode));


        // 자식 테이블 데이터 먼저 삭제(workplace_code 참조하는 데이터 제거)
        workplaceScheduleRepository.deleteByWorkplaceCode(workplaceCode);
        // 부모 데이터 삭제
        workplaceRepository.delete(workplace);

    }
}
