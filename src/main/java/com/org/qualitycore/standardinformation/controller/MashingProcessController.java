package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.MashingProcessDTO;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.standardinformation.model.service.MashingProcessService;
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
@RequestMapping("/mashingprocess")
@CrossOrigin(origins ="http://localhost:3000" )
@RequiredArgsConstructor
@Tag(name="MashingProcess" , description = "당화 공정 API")
@Slf4j
public class MashingProcessController {

    private final MashingProcessService mashingProcessService;


    //분쇄공정 등록
    @Operation(summary = "당화공정" , description = "당화공정 작업을 등록합니다")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "201" , description = "등록에 성공!!"),
            @ApiResponse(responseCode = "400" , description = "잘못된 요청입니다.")
    })
    @PostMapping("/register")
    public ResponseEntity<ErpMessage>createMashingProcess(
            @RequestBody @Parameter(description = "등록할 당화 정보" ,required = true)
            MashingProcessDTO mashingProcessDTO){
        log.info("컨트롤러 : 분쇄공정 등록 요청 {}" , mashingProcessDTO);
        ErpMessage response =mashingProcessService.createMashingProcess(mashingProcessDTO);
        return ResponseEntity.status(response.getHttpStatusCode()).body(response);
    }



    // 실제 종료시간을  위해서 수정 추가 구현
    @PutMapping("/{mashingId}")
    public ResponseEntity<MashingProcessDTO> completeMashingProcess(
            @PathVariable String mashingId) {
        log.info("컨트롤러 : 분쇄 공정 완료 요청 - ID: {}", mashingId);
        MashingProcessDTO updatedMashing = mashingProcessService.completeMashingProcess(mashingId);
        return ResponseEntity.ok(updatedMashing);
    }


}
