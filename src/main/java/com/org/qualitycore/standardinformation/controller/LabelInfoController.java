package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.LabelInfoDTO;
import com.org.qualitycore.standardinformation.model.service.LabelInfoService;
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
public class LabelInfoController {

    private final LabelInfoService labelInfoService;

    // 라벨전체조회
    @GetMapping("/labelInfo")
    public ResponseEntity<Message> findAllLabelInfo() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<LabelInfoDTO> labelInfo = labelInfoService.findAllLabelInfo();

        Map<String, Object> res = new HashMap<>();
        res.put("labelInfo", labelInfo);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "전체조회완료", res));
    }

    // 상세조회
    @GetMapping("/labelInfo/{labelId}")
    public ResponseEntity<Message> findByIdLabelInfo(@PathVariable("labelId") String labelId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        LabelInfoDTO labelInfo = labelInfoService.findByIdLabelInfo(labelId);

        Map<String, Object> res = new HashMap<>();
        res.put("labelInfo", labelInfo);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "상세조회완료", res));
    }

    // 등록
    @PostMapping("/labelInfo")
    public ResponseEntity<?> createLabelInfo(@RequestBody LabelInfoDTO labelInfo) {

        labelInfoService.createLabelInfo(labelInfo);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 201);
        res.put("message", "등록성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 수정
    @PutMapping("/labelInfo")
    public ResponseEntity<?> updateLabelInfo(@RequestBody LabelInfoDTO labelInfo) {

        labelInfoService.updateLabelInfo(labelInfo);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 201);
        res.put("message", "수정성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 삭제
    @DeleteMapping("/labelInfo/{labelId}")
    public ResponseEntity<?> deleteLabelInfo(@PathVariable("labelId") String labelId) {

        labelInfoService.deleteLabelInfo(labelId);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 201);
        res.put("message", "삭제성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
