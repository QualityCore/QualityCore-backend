package com.org.qualitycore.masterdata.model.service;

import com.org.qualitycore.masterdata.model.dto.WorkplaceDTO;
import com.org.qualitycore.masterdata.model.entity.Workplace;
import com.org.qualitycore.masterdata.model.repository.MasterDataRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final MasterDataRepository masterDataRepository;
    private final ModelMapper modelMapper;

    // 작업장 전체 조회
    public List<Workplace> getAllWorkplaces() {
        return masterDataRepository.findAll();
    }

    //작업장 등록
    public Workplace creactWorkplace(WorkplaceDTO workplaceDTO) {
        Workplace workplace = modelMapper.map(workplaceDTO,Workplace.class); // DTO->엔티티변환
        return masterDataRepository.save(workplace);
    }


    public Workplace updateWorkplace(int id, WorkplaceDTO workplaceDTO) {
        Workplace workplace = masterDataRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 작업장은 존재하지 않아요! ID:" +id));
        // DTO 받은 값이 null 이 아니면 업데이드 ㄱ
        Workplace updateWorkplace = workplace.toBuilder()
                .workplaceName(workplaceDTO.getWorkplaceName() !=null ? workplaceDTO.getWorkplaceName() : workplace.getManagerName())
                .workplaceType(workplaceDTO.getWorkplaceType() !=null ? workplaceDTO.getWorkplaceType() : workplace.getWorkplaceType())
                .workplaceStatus(workplaceDTO.getWorkplaceStatus() !=null ? workplaceDTO.getWorkplaceStatus() : workplace.getWorkplaceStatus())
                .workplaceLocation(workplaceDTO.getWorkplaceLocation() !=null ? workplaceDTO.getWorkplaceLocation() : workplace.getWorkplaceLocation())
                .managerName(workplaceDTO.getManagerName() !=null ? workplaceDTO.getManagerName() : workplace.getManagerName())
                .workplaceCapacity(workplaceDTO.getWorkplaceCapacity() !=0 ? workplaceDTO.getWorkplaceCapacity() : workplace.getWorkplaceCapacity() )
                .build();
        return masterDataRepository.save(updateWorkplace);
    }
}
