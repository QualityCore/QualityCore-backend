package com.org.qualitycore.work.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.service.WorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "WorkOrder(작업지시서)", description = "작업지시서 API_Controller")
public class WorkController {

    private final WorkService workService;

    // 작업지시서 전체 조회
    @GetMapping("/work")
    @Operation(summary = "작업지시서 전체 조회", description = "작업지시서를 전체 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작업지시서 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "작업지시서가 없습니다.")})
    public ResponseEntity<Message> findAllWorkOrders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<WorkFindAllDTO> work = workService.findAllWorkOrders();

        // 작업지시서가 없을경우
        if (work == null || work.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new Message(404, "작업지시서가 없습니다.", null));
        }

        // 전체 조회 성공 시
        Map<String, Object> res = new HashMap<>();
        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "작업지시서 전체조회 성공", res));
    }


    // 작업지시서 상세 조회
    @GetMapping("/work/{lotNo}")
    @Operation(summary = "직원 스케줄 전체 조회", description = "직원 한 명의 전체 스케줄을 조회합니다.",
            parameters = {@Parameter(name = "empId", description = "직원 한명의 대한 전체스케줄에 필요한 고유 PK")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "스케줄 데이터가 없습니다.")})
    public ResponseEntity<Message> findByCodeWorkOrder(@PathVariable("lotNo") String lotNo) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        WorkFindAllDTO work = workService.findByCodeWorkOrder(lotNo);
        // 작업지시서가 있을 경우 상세 조회
        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "작업지시서 상세조회 성공", res));
    }


    // 작업지시서 등록
    @PostMapping("/work")
    @Operation(summary = "직원 스케줄 전체 조회", description = "직원 한 명의 전체 스케줄을 조회합니다.",
            parameters = {@Parameter(name = "empId", description = "직원 한명의 대한 전체스케줄에 필요한 고유 PK")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "스케줄 데이터가 없습니다.")})
    public ResponseEntity<?> workOrderCreate(@RequestBody WorkFindAllDTO work) {

        workService.createWorkOrder(work);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 201);
        res.put("message", "작업지시서 생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 작업지시서 삭제
    @DeleteMapping("/work/{lotNo}")
    @Operation(summary = "직원 스케줄 전체 조회", description = "직원 한 명의 전체 스케줄을 조회합니다.",
            parameters = {@Parameter(name = "empId", description = "직원 한명의 대한 전체스케줄에 필요한 고유 PK")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스케줄 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "스케줄 데이터가 없습니다.")})
    public ResponseEntity<?> workOrderDelete(@PathVariable("lotNo") String lotNo) {

        workService.workOrderDelete(lotNo);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 200);
        res.put("message", "작업지시서 삭제 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }


}
