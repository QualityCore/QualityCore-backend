package com.org.qualitycore.masterdata.controller;

import com.org.qualitycore.masterdata.model.dto.WorkplaceDTO;
import com.org.qualitycore.masterdata.model.entity.MasterDataMessage;
import com.org.qualitycore.masterdata.model.entity.Workplace;
import com.org.qualitycore.masterdata.model.service.MasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/masterdata")
@RequiredArgsConstructor
@Tag(name="Master Data" , description = "기준정보관리 API")
public class MasterDataController {

    private final MasterDataService masterDataService;
    private final ModelMapper modelMapper;


    // 작업장 전체 조회
    @Operation(summary="작업장정보 전체조회", description = "작업장 정보를 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "성공적으로 작업장 목록을 조회를 합니다.",
                    content= @Content(array = @ArraySchema(schema = @Schema(implementation = WorkplaceDTO.class))))})
    @GetMapping("/workplaces/find")
    public List<WorkplaceDTO> getAllWorkplaces() {
        List<Workplace> workplaces = masterDataService.getAllWorkplaces();
        return workplaces.stream()
                .map(workplace -> modelMapper.map(workplace, WorkplaceDTO.class))
                .collect(Collectors.toList());
    }


    // 작업장 등록
    @Operation(summary = "작업장정보 등록", description = "새로운 작업장을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작업장 등록 성공",
                    content = @Content(schema = @Schema(implementation = MasterDataMessage.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")})
    @PostMapping("/workplaces/regist")
    public ResponseEntity<MasterDataMessage> createWorkplace(
            @RequestBody @Parameter(description = "등록할 작업장 정보",required = true) WorkplaceDTO workplaceDTO) {
        Workplace savedWorkplace = masterDataService.creactWorkplace(workplaceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MasterDataMessage(HttpStatus.CREATED.value(),
                        "등록에 성공했어!! 축하해! ID :" + savedWorkplace.getWorkplaceId()));
    }


    // 작업장 수정
    @Operation(summary = "작업장정보 수정" , description = "장업장 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "작업장 수정 성공!",
                    content = @Content(schema = @Schema(implementation = MasterDataMessage.class))),
            @ApiResponse(responseCode = "404" , description = "해당 ID의 작업장을 찾을수없어요")})
    @PutMapping("/workplaces/{id}")
    public ResponseEntity<MasterDataMessage> updateWorkplace(
            @PathVariable @Parameter(description = "수정할 작업장의 ID",required = true)  int id,
            @RequestBody @Parameter(description = "수정할 작업장 정보",required = true) WorkplaceDTO workplaceDTO){
        Workplace updateWorkplace = masterDataService.updateWorkplace(id ,workplaceDTO);
        return  ResponseEntity.ok(new MasterDataMessage(HttpStatus.OK.value(),
                "수정이 완료되었어요 짝짝!!" + updateWorkplace.getWorkplaceId()));
    }


    // 작업장 등록 삭제
    @Operation(summary = "작업장정보 삭제",description = "작업장정보를 삭제 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "작업장 삭제 성공",
                        content = @Content(schema = @Schema(implementation = MasterDataMessage.class))),
            @ApiResponse(responseCode = "404",description = "해당 ID의 작업장을 찾을 수 없음")})
    @DeleteMapping("/workplaces/{id}")
    public ResponseEntity<MasterDataMessage> deleteWorkplace(
            @PathVariable @Parameter(description ="삭제할 장업장의 ID" , required = true) int id) {
        masterDataService.deleteWorkplace(id);
        return ResponseEntity.ok(new MasterDataMessage(HttpStatus.OK.value(),
                "삭제 성공! ID: " + id));
    }


    }













