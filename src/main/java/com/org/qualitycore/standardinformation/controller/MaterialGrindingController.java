package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.LineMaterialNDTO;
import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.ErpMessage;
import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.standardinformation.model.service.MaterialGrindingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/productionprocess")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name="ProductionProcess" , description = "생산공정 API")
@Slf4j
public class MaterialGrindingController {

    private final MaterialGrindingService materialGrindingService;


    // ✅ 작업지시 ID 목록 조회 API (DTO 적용)
    @Operation(summary = "작업지시 ID 목록 조회", description = "현재 등록된 작업지시 ID 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/linematerial")
    public ResponseEntity<List<LineMaterialNDTO>> getLineMaterial() {
        log.info("컨트롤러: 작업지시 ID 목록 조회 요청");
        List<LineMaterialNDTO> lineMaterials = materialGrindingService.getLineMaterial();
        return ResponseEntity.ok(lineMaterials);
    }


    // ✅ 특정 LOT_NO에 대한 자재 정보 조회 API (DTO 적용)
    @Operation(summary = "LOT_NO에 따른 자재 정보 조회", description = "특정 작업지시 ID(LOT_NO)에 대한 자재 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "데이터 없음")
    })
    @GetMapping("/{lotNo}")
    public ResponseEntity<List<LineMaterialNDTO>> getMaterialsByLotNo(@PathVariable String lotNo) {
        log.info("컨트롤러: LOT_NO={}에 대한 자재 정보 요청", lotNo);
        List<LineMaterialNDTO> materials = materialGrindingService.getMaterialsByLotNo(lotNo);

        if (materials.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(materials);
    }




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
            return ResponseEntity.status(response.getHttpStatusCode()).body(response);
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