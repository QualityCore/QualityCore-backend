package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.ProcessInfoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessInfoService {

    private final ModelMapper modelMapper;

    public List<ProcessInfoDTO> processFindAll() {



        return null;
    }
}
