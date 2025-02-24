package com.org.qualitycore.attendance.controller;

import com.org.qualitycore.attendance.model.dto.AttendanceDTO;
import com.org.qualitycore.attendance.model.dto.EmpScheduleCreateDTO;
import com.org.qualitycore.attendance.model.service.ScheduleService;
import com.org.qualitycore.common.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Schedule(스케줄)", description = "스케줄 API_Controller")
public class ScheduleController {

    private final ScheduleService scheduleService;


    // 달력에 표시될 여러 스케줄
    @GetMapping("/scheduleAll/{empId}")
    @Operation(summary = "직원 스케줄 전체 조회", description = "직원 한 명의 전체 스케줄을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "스케줄 데이터가 없습니다.")})
    public ResponseEntity<Message> findAllSchedulesByEmpId(@PathVariable("empId") String empId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 직원 스케줄 전체 조회
        List<AttendanceDTO> schedule = scheduleService.findAllSchedulesByEmpId(empId);

        // 전체조회할 데이터가 없을 경우 예외처리
        if (schedule.isEmpty()) {

            Map<String, Object> res = new HashMap<>();
            res.put("status", 404);
            res.put("message", "스케줄 데이터가 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new Message(404, "스케줄 데이터가 없습니다.", res));
        }

        // 전체 조회 성공 시
        Map<String, Object> res = new HashMap<>();
        res.put("schedule", schedule);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "스케줄 전체조회 성공", res));
    }


    // 스케줄 상세페이지
    @GetMapping("/schedule/{scheduleId}")
    @Operation(summary = "직원 스케줄 상세 조회", description = "직원 한 명의 날짜를 선택하면 상세정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "근태 상세조회 성공"),
            @ApiResponse(responseCode = "404", description = "스케줄을 찾을 수 없습니다.")})
    public ResponseEntity<Message> findByEmpId(@PathVariable("scheduleId") String scheduleId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 직원 스케줄 상세 조회
        AttendanceDTO schedule = scheduleService.findByEmpId(scheduleId);

        // 스케줄 데이터가 없을 경우 예외처리
        if (schedule == null) {
            Map<String, Object> res = new HashMap<>();
            res.put("status", 404);
            res.put("message", "스케줄을 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new Message(404, "스케줄을 찾을 수 없습니다.", res));
        }

        // 스케줄 상세 조회 성공 시
        Map<String, Object> res = new HashMap<>();
        res.put("schedule", schedule);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "근태 상세조회 성공", res));
    }

    // 스케줄 등록
    @PostMapping("/schedule")
    @Operation(summary = "직원 스케줄 등록", description = "직원의 스케줄을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "스케줄 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")})
    public ResponseEntity<?> createSchedule(@Valid @RequestBody EmpScheduleCreateDTO schedule, BindingResult bindingResult) {

        // 필드 유효성 검사 실패 시 처리
        if (bindingResult.hasErrors()) {
            Map<String, Object> res = new HashMap<>();
            res.put("status", 400);

            // 유효성 검증 실패한 필드 메시지 추출
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            });

            res.put("message", "잘못된 요청입니다. " + errorMessage.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        try {
            // 스케줄 생성 서비스 호출
            scheduleService.createSchedule(schedule);

            // 성공 시 응답
            Map<String, Object> res = new HashMap<>();
            res.put("status", 201);
            res.put("message", "스케줄 생성 성공");

            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (Exception e) {
            // 예외 발생 시 처리
            Map<String, Object> res = new HashMap<>();
            res.put("status", 400);
            res.put("message", "잘못된 요청입니다. 입력값을 확인해주세요.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }



    // 스케줄 수정
    @PutMapping("/schedule")
    @Operation(summary = "직원 스케줄 수정", description = "직원의 스케줄을 수정합니다.")
    public ResponseEntity<?> updateSchedule(@RequestBody EmpScheduleCreateDTO schedule) {

        scheduleService.updateSchedule(schedule);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 201);

        res.put("message", "스케줄 수정 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 스케줄 삭제
    @DeleteMapping("/schedule/{scheduleId}")
    @Operation(summary = "직원 스케줄 삭제", description = "직원의 스케줄을 삭제합니다.")
    public ResponseEntity<?> deleteSchedule(@PathVariable("scheduleId") String scheduleId) {

        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.created(URI.create("api/v1/scheduleAll/")).body(new Message(200, "삭제성공!", null));
    }

}
