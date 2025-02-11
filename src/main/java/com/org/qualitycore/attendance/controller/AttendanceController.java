package com.org.qualitycore.attendance.controller;

import com.org.qualitycore.attendance.model.service.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance(근태)", description = "근태 API_Controller")
public class AttendanceController {

    private final AttendanceService attendanceService;
}
