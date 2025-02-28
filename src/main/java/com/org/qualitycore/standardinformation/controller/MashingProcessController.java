package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.MashingProcessDTO;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.standardinformation.model.service.MashingProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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



    // 당화공정 pH 값 및 실제종료시간 업데이트
    @Operation(
            summary = "당화 공정 완료",
            description = "주어진 ID의 당화 공정을 완료하고 pH 값을 업데이트합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 공정이 완료됨",
                    content = @Content(schema = @Schema(implementation = MashingProcessDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다")})
    @PutMapping("/register/{mashingId}")
    public ResponseEntity<MashingProcessDTO> completeMashingProcess(
            @PathVariable @Parameter(description = "완료할 당화 공정의 ID", required = true) String mashingId,
            @RequestBody @Parameter(description = "수정할 당화 공정 정보 (pH 값 포함)", required = true)
            Map<String, Double> requestBody) {
        log.info("컨트롤러 : 당화 공정 완료 요청 - ID {} , 요청 데이터 {} ", mashingId, requestBody);
        Double phValue = requestBody.get("phValue"); // 요청에서 pH 값 추출
        MashingProcessDTO updatedMashing = mashingProcessService.completeMashingProcess(mashingId, phValue);
        return ResponseEntity.ok(updatedMashing);
    }


}
