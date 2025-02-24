package com.org.qualitycore.work.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.service.WorkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<Message> findAllWorkOrders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 작업지시서 전체 조회
        List<WorkFindAllDTO> work = workService.findAllWorkOrders();

        // 전체 조회 성공 시
        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "작업지시서 전체조회 성공", res));
    }


    // 작업지시서 상세 조회
    @GetMapping("/work/{lotNo}")
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

}
