package com.org.qualitycore.standardinformation.controller;


import com.org.qualitycore.common.Message;
import com.org.qualitycore.standardinformation.model.dto.BoilingProcessDTO;
import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.service.BoilingProcessService;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boilingprocess")
@CrossOrigin(origins ="http://localhost:3000" )
@RequiredArgsConstructor
@Tag(name="BoilingProcess" , description = "끓임 공정 API")
@Slf4j
public class BoilingProcessController {


    private final BoilingProcessService boilingProcessService;


    // ✅ 작업지시 ID 목록 조회
    @Operation(summary = "작업지시 ID 목록 조회", description = "현재 등록된 작업지시 ID 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/linematerial")
    public ResponseEntity<Message> getLineMaterial() {
        log.info("컨트롤러: 작업지시 ID 목록 조회 요청");
        List<LineMaterialNDTO> lineMaterials = boilingProcessService.getLineMaterial();

        Message response = new Message(200, "작업지시 ID 목록 조회 성공", new HashMap<>());
        response.getResult().put("lineMaterials", lineMaterials);

        return ResponseEntity.ok(response);
    }



    // ✅ 특정 LOT_NO에 대한 자재 정보 조회
    @Operation(summary = "LOT_NO에 따른 자재 정보 조회", description = "작업지시 ID 로 특정 자재 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @GetMapping("/{lotNo}")
    public ResponseEntity<Message> getMaterialsByLotNo(@PathVariable String lotNo) {
        log.info ("컨트롤러: 끓임 자재정보 LOT_NO={}에 대한 자재 정보 요청", lotNo);
        List<LineMaterialNDTO> materials = boilingProcessService.getMaterialsByLotNo(lotNo);
        Message response;
        if (materials.isEmpty()) {
            response = new Message(404, "데이터 없음", new HashMap<>());
        } else {
            response = new Message(200, "작업지시 ID 로 특정 자재 정보를 조회성공", new HashMap<>());
            response.getResult().put("materials", materials);
        }
        return ResponseEntity.status(response.getCode()).body(response);
    }



    //끓임공정 등록
    @Operation(summary = "끓임공정" , description = "끓임공정 작업을 등록합니다")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "201" , description = "등록에 성공!!"),
            @ApiResponse(responseCode = "400" , description = "잘못된 요청입니다.")
    })
    @PostMapping("/register")
    public ResponseEntity<Message> createBoilingProcess(
            @RequestBody @Parameter(description = "등록할 끓임 정보", required = true)
            BoilingProcessDTO boilingProcessDTO) {
        log.info("컨트롤러 : 끓임공정 등록 요청 {}", boilingProcessDTO);
        Message response = boilingProcessService.createBoilingProcess(boilingProcessDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }



    // 끓임 공정 끓임 후 워트량 , 끓임손실량 , 실제종료시간 수정 구문

    @Operation(
            summary = "끓임 공정 업데이트",
            description = "주어진 ID의 끓임 공정에서 끓음 후 워트량, 끓음 손실량 및 실제 종료 시간을 업데이트합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 업데이트됨",
                    content = @Content(schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다")})
    @PutMapping("/update/{boilingId}")
    public ResponseEntity<Message> updateBoilingProcess(
            @PathVariable @Parameter(description = "업데이트할 끓임 공정의 ID", required = true) String boilingId,
            @RequestBody @Parameter(description = "수정할 끓임 공정 정보", required = true)
            Map<String, Object> requestBody) {
        log.info("컨트롤러 : 끓임 공정 업데이트 요청 - ID {}, 요청 데이터 {}", boilingId, requestBody);

        Object postBoilWortVolumeObj = requestBody.get("postBoilWortVolume");
        Double postBoilWortVolume = (postBoilWortVolumeObj instanceof Number number)
                ? number.doubleValue()
                : null;

        Object boilLossVolumeObj = requestBody.get("boilLossVolume");
        Double boilLossVolume = (boilLossVolumeObj instanceof Number number)
                ? number.doubleValue()
                : null;



        Message response = boilingProcessService.updateBoilingProcess(boilingId, postBoilWortVolume, boilLossVolume);
        return ResponseEntity.status(response.getCode()).body(response);

    }



    // 홉 투입 업데이트 구문
    @Operation(
            summary = "홉 투입 정보 업데이트",
            description = "타이머 이벤트에 따라 홉 투입 정보를 업데이트합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 업데이트됨",
                    content = @Content(schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다")
    })
    @PutMapping("/hop/{boilingId}")
    public ResponseEntity<Message> updateHopInfo(
            @PathVariable @Parameter(description = "업데이트할 끓임 공정의 ID", required = true) String boilingId,
            @RequestBody @Parameter(description = "수정할 홉 투입 정보", required = true)
            Map<String, Object> requestBody) {

        log.info("컨트롤러 : 홉 정보 업데이트 요청 - ID {}, 요청 데이터 {}", boilingId, requestBody);

        Object firstHopNameObj = requestBody.get("firstHopName");
        String firstHopName = (firstHopNameObj instanceof String) ? (String) firstHopNameObj : null;

        Object firstHopAmountObj = requestBody.get("firstHopAmount");
        Double firstHopAmount = (firstHopAmountObj instanceof Number number) ? number.doubleValue() : null;

        Object secondHopNameObj = requestBody.get("secondHopName");
        String secondHopName = (secondHopNameObj instanceof String) ? (String) secondHopNameObj : null;

        Object secondHopAmountObj = requestBody.get("secondHopAmount");
        Double secondHopAmount = (secondHopAmountObj instanceof Number number) ? number.doubleValue() : null;

        Message response = boilingProcessService.updateHopInfo(
                boilingId, firstHopName, firstHopAmount, secondHopName, secondHopAmount
        );

        return ResponseEntity.status(response.getCode()).body(response);
    }









    // ✅ 특정 LOT_NO에 대한 끓임 공정 상태 업데이트
    @Operation(summary = "LOT_NO에 따른 끓임 공정 상태 업데이트", description = "LOT_NO를 기준으로 공정 상태를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "해당 LOT_NO 없음")
    })
    @PutMapping("/update")
    public ResponseEntity<Message> updateBoilingProcessStatus
    (@RequestBody BoilingProcessDTO boilingProcessDTO) {

        log.info("컨트롤러: LOT_NO={} 끓임 공정 상태 업데이트 요청 - 데이터: {}",
                boilingProcessDTO.getLotNo(), boilingProcessDTO);

        Message response = boilingProcessService.updateBoilingProcessStatus(boilingProcessDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }





}
