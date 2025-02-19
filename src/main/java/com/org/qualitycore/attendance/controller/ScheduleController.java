package com.org.qualitycore.attendance.controller;

import com.org.qualitycore.attendance.model.dto.AttendanceDTO;
import com.org.qualitycore.attendance.model.dto.ScheduleMessage;
import com.org.qualitycore.attendance.model.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;


    @GetMapping("/list")
    public ResponseEntity<ScheduleMessage> findAllSchedule() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<AttendanceDTO> schedule = scheduleService.findAllSchedule();

        Map<String, Object> res = new HashMap<>();

        System.out.println("값확인 = " + res);

        res.put("schedule", schedule);

        return ResponseEntity.ok().headers(headers).body(new ScheduleMessage(200, "전체조회 성공", res));
    }

}
