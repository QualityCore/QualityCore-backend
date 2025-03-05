package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.EquipmentInfoDTO;
import com.org.qualitycore.standardinformation.model.service.EquipmentInfoService;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EquipmentInfoController {

    private final EquipmentInfoService equipmentInfoService;

    // 설비 전체조회
    @GetMapping("/equipment")
    public ResponseEntity<Message> findEquipmentAll(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<EquipmentInfoDTO> equipment = equipmentInfoService.findEquipmentAll();

        Map<String, Object> res = new HashMap<>();
        res.put("equipment", equipment);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "전체조회성공", res));
    }

    // 설비 상세조회
    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<Message> findByCodeEquipment(@PathVariable("equipmentId") String equipmentId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        EquipmentInfoDTO equipment = equipmentInfoService.findByCodeEquipment(equipmentId);

        Map<String, Object> res = new HashMap<>();
        res.put("equipment", equipment);

        return ResponseEntity.ok().headers(headers).body(new Message(200, "상세조회성공", res));
    }

    // 설비등록
    @PostMapping("/equipment")
    public ResponseEntity<?> createEquipment(@RequestBody EquipmentInfoDTO equipment) {

        equipmentInfoService.createEquipment(equipment);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 201);

        res.put("message", "설비 등록 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 설비수정
    @PutMapping("/equipment")
    public ResponseEntity<?> updateEquipment(@RequestBody EquipmentInfoDTO equipment) {

        equipmentInfoService.updateEquipment(equipment);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 201);

        res.put("message", "설비 수정 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // 설비삭제
    @DeleteMapping("/equipment")
    public ResponseEntity<?> deleteEquipment(@PathVariable("equipmentId") String equipmentId) {

        equipmentInfoService.deleteEquipment(equipmentId);

        Map<String, Object> res = new HashMap<>();

        res.put("status", 201);

        res.put("message", "설비 삭제 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

}
