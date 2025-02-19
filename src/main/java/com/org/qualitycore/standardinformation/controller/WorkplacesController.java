package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.WorkplaceDTO;
import com.org.qualitycore.standardinformation.model.entity.StandardInformationMessage;
import com.org.qualitycore.standardinformation.model.service.StandardInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/standardinformation")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name="Workplace" , description = "작업장정보 API")
public class WorkplacesController {

    private final StandardInformationService standardInformationService;



    // 작업장 전체 조회
    @Operation(summary = "작업장정보 전체조회", description = "모든 작업장 정보를 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 작업장 목록을 조회를 합니다.")})

    @GetMapping("/workplaces/find")
    public List<WorkplaceDTO> getAllWorkplaces() {
        return standardInformationService.getAllWorkplaces();
    }


    // 작업장 등록
    @Operation(summary = "작업장정보 등록", description = "새로운 작업장을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작업장 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")})
    @PostMapping("/workplaces/regist")
    public ResponseEntity<StandardInformationMessage> createWorkplace(
            @RequestBody @Parameter(description = "등록할 작업장 정보", required = true)
            WorkplaceDTO workplaceDTO) {
        standardInformationService.createWorkplace(workplaceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardInformationMessage(HttpStatus.CREATED.value(), "작업장 등록 성공! 짝짝짝"));
    }


    // 작업장 수정
    @Operation(summary = "작업장정보 수정", description = "장업장 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업장 수정 성공!"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 작업장을 찾을수없어요")})
    @PutMapping("/workplaces/{id}")
    public ResponseEntity<StandardInformationMessage> updateWorkplace(
            @PathVariable @Parameter(description = "수정할 작업장의 ID", required = true) String id,
            @RequestBody @Parameter(description = "수정할 작업장 정보", required = true) WorkplaceDTO workplaceDTO) {
        standardInformationService.updateWorkplace(id, workplaceDTO);
        return ResponseEntity.ok(new StandardInformationMessage
                (HttpStatus.OK.value(), "작업장 수정 완료 ID :" + id));
    }


    // 작업장 등록 삭제
    @Operation(summary = "작업장정보 삭제", description = "작업장정보를 삭제 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업장 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 작업장을 찾을 수 없음")})

    @DeleteMapping("/workplaces/{id}")
    public ResponseEntity<StandardInformationMessage> deleteWorkplace(
            @PathVariable @Parameter(description = "삭제할 장업장의 ID", required = true) String id) {

        standardInformationService.deleteWorkplace(id);
        return ResponseEntity.ok(new StandardInformationMessage(HttpStatus.OK.value(),
                    "작업장 삭제 완료! ID: " + id));

        }
}















