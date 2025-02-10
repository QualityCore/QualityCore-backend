package com.org.qualitycore.work.controller;

import com.org.qualitycore.work.model.entity.WorkMessage;
import com.org.qualitycore.work.model.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
@RequestMapping("/work")
public class WorkController {

    private final WorkService workService;

    @GetMapping("/list")
    public ResponseEntity<WorkMessage> findAllWork() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));


        return ResponseEntity.ok().headers(headers).body(new WorkMessage(200, "작업지시서 전체조회 성공", null));
    }
}
