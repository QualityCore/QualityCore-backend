package com.org.qualitycore.attendance.controller;

import com.org.qualitycore.attendance.model.dto.AttendanceDTO;
import com.org.qualitycore.attendance.model.dto.EmpScheduleCreateDTO;
import com.org.qualitycore.attendance.model.dto.ScheduleMessage;
import com.org.qualitycore.attendance.model.service.ScheduleService;
import com.org.qualitycore.common.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ScheduleController {

    private final ScheduleService scheduleService;


    @GetMapping("/schedule")
    public ResponseEntity<Message> findAllSchedule() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<AttendanceDTO> schedule = scheduleService.findAllSchedule();

        System.out.println("schedule = " + schedule);

        Map<String, Object> res = new HashMap<>();

        res.put("schedule", schedule);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "전체조회 성공", res));
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<Message> findByCodeSchedule(@PathVariable("scheduleId") String scheduleId) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        AttendanceDTO schedule = scheduleService.findByCodeSchedule(scheduleId);

        Map<String, Object> res = new HashMap<>();

        res.put("schedule", schedule);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "근태 상세조회 성공", res));
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> createSchedule(@RequestBody EmpScheduleCreateDTO schedule) {

        scheduleService.createSchedule(schedule);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 201);

        res.put("message", "스케줄 생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

}
