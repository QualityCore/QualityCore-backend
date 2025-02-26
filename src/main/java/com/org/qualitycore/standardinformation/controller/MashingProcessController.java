package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.service.MashingProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mashingprocess")
@CrossOrigin(origins ="http://localhost:3000" )
@RequiredArgsConstructor
@Tag(name="MashingProcess" , description = "당화 공정 API")
@Slf4j
public class MashingProcessController {

    private final MashingProcessService mashingProcessService;








}
