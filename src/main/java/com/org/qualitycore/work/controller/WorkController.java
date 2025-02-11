package com.org.qualitycore.work.controller;

import com.org.qualitycore.work.model.dto.WorkDTO;
import com.org.qualitycore.work.model.entity.WorkMessage;
import com.org.qualitycore.work.model.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;

    // 작업지시서 전체 조회
    @GetMapping("/list")
    public ResponseEntity<WorkMessage> findAllWorkOrders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<WorkDTO> work = workService.findAllWorkOrders();

        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok().
                headers(headers).
                body(new WorkMessage(200, "작업지시서 전체조회 성공", res));
    }

    // 작업지시서 상세 조회
    @GetMapping("/detail/{workOrderId}")
    public ResponseEntity<WorkMessage> findByWorkOrderCode(@PathVariable("workOrderId") int workId) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        WorkDTO work = workService.findByWorkOrderCode(workId);

        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok().
                headers(headers).
                body(new WorkMessage(200, "작업지시서 상세조회 성공", res));
    }

    // 작업지시서 생성
    @PostMapping("/create")
    public ResponseEntity<?> workOrderCreate(@RequestBody WorkDTO work) {

        workService.workOrderCreate(work);

        Map<String, Object> response = new HashMap<>();

        response.put("status", 201);

        response.put("message", "작업지시서 생성 성공");

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(response);
    }

    // 작업지시서 수정
    @PutMapping("/update")
    public ResponseEntity<?> workOrderUpdate(@RequestBody WorkDTO work) {

        workService.workOrderUpdate(work);

        Map<String, Object> response = new HashMap<>();

        response.put("status", 200);

        response.put("message", "작업지시서 수정 성공");

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(response);

    }

    // 작업지시서 삭제
    @DeleteMapping("/detail/delete/{workOrderId}")
    public ResponseEntity<?> workOrderDelete(@PathVariable("workOrderId") int workId) {

        workService.workOrderDelete(workId);

        Map<String, Object> response = new HashMap<>();

        response.put("status", 200);

        response.put("message", "작업지시서 삭제 성공");

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(response);
    }
}
