package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.ProcessInfoDTO;
import com.org.qualitycore.standardinformation.model.service.ProcessInfoService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProcessInfoController {

    private final ProcessInfoService processInfoService;

    @GetMapping("/process")
    public ResponseEntity<Message> processFindAll() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Applicaiton", "json", Charset.forName("UTF-8")));

        List<ProcessInfoDTO> processInfo = processInfoService.processFindAll();

        Map<String, Object> res = new HashMap<>();
        res.put("processInfo", processInfo);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "전체조회 성공", res));
    }


}
