package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.MaterialGrindingDTO;
import com.org.qualitycore.standardinformation.model.entity.StandardInformationMessage;
import com.org.qualitycore.standardinformation.model.service.ProductionProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productionprocess")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name="ProductionProcess" , description = "생산공정 API")
public class ProductionProcessController {

    private final ProductionProcessService productionProcessService;



    //분쇄공정
    @Operation(summary = "분쇄공정" ,description = "분쇄공정 작업을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201" , description = "등록에 성공!"),
            @ApiResponse(responseCode = "400" , description = "잘못된 요청입니다!")
    })
    @PostMapping("/materialgrinding")
    public ResponseEntity<StandardInformationMessage> createMaterialGrinding(
            @RequestBody @Parameter(description = "등록할 분쇄 정보" ,
                    required = true) MaterialGrindingDTO materialGrindingDTO){
        StandardInformationMessage response = productionProcessService.createMaterialGrinding(materialGrindingDTO);
            return ResponseEntity.status(response.getHttpStatusCode())
                    .body(response);
    }



}
