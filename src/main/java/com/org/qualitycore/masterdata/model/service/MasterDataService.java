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
}
