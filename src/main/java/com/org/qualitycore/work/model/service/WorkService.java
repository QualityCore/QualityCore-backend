package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final ModelMapper modelMapper;
    private final WorkRepository workRepository;
}
