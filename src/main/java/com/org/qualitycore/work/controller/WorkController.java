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

    @GetMapping("/list") // 작업지시서 전체 조회
    public ResponseEntity<WorkMessage> findAllWorkOrders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<WorkDTO> work = workService.findAllWorkOrders();

        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok().headers(headers).body(new WorkMessage(200, "작업지시서 전체조회 성공", res));
    }

}
