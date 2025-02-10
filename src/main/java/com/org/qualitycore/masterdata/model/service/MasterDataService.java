package com.org.qualitycore.masterdata.model.service;

import com.org.qualitycore.masterdata.model.entity.Workplace;
import com.org.qualitycore.masterdata.model.repository.MasterDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterDataService {

    private final MasterDataRepository masterDataRepository;

    public List<Workplace> getAllWorkplaces() {
        return masterDataRepository.findAll();
    }
}
