package com.org.qualitycore.attendance.model.service;

import com.org.qualitycore.attendance.model.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ModelMapper modelMapper;
}
