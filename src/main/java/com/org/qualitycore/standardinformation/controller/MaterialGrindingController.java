package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.standardinformation.model.service.MaterialGrindingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/productionprocess")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name="ProductionProcess" , description = "생산공정 API")
@Slf4j
public class MaterialGrindingController {

    private final MaterialGrindingService materialGrindingService;


    //분쇄공정 등록
    @Operation(summary = "분쇄공정" ,description = "분쇄공정 작업을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "등록에 성공!"),
            @ApiResponse(responseCode = "400" , description = "잘못된 요청입니다!")
    })
    @PostMapping("/materialgrinding")
    public ResponseEntity<ErpMessage> createMaterialGrinding(
            @RequestBody @Parameter(description = "등록할 분쇄 정보" ,
                    required = true) MaterialGrindingDTO materialGrindingDTO){
            log.info("컨트롤러 : 분쇄공정 등록 요청 {} " ,materialGrindingDTO);
            ErpMessage response = materialGrindingService.createMaterialGrinding(materialGrindingDTO);
            return ResponseEntity.status(response.getHttpStatusCode())
                    .body(response);
        }



        // 실제 종료시간을  위해서 수정 추가 구현
        @PutMapping("/materialgrinding/{grindingId}")
        public ResponseEntity<MaterialGrindingDTO> completeGrindingProcess(
                @PathVariable String grindingId) {
            log.info("컨트롤러 : 분쇄 공정 완료 요청 - ID: {}", grindingId);
            MaterialGrindingDTO updatedGrinding = materialGrindingService.completeGrindingProcess(grindingId);
            return ResponseEntity.ok(updatedGrinding);
        }
    }