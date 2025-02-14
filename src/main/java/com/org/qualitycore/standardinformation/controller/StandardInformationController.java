package com.org.qualitycore.standardinformation.controller;

import com.org.qualitycore.standardinformation.model.dto.WorkplaceDTO;
import com.org.qualitycore.standardinformation.model.entity.StandardInformationMessage;
import com.org.qualitycore.standardinformation.model.entity.Workplace;
import com.org.qualitycore.standardinformation.model.service.StandardInformationService;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/standardinformation")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name="Master Data" , description = "기준정보관리 API")
public class StandardInformationController {

    private final StandardInformationService standardInformationService;
    private final ModelMapper modelMapper;


    // 작업장 전체 조회
    @Operation(summary = "작업장정보 전체조회", description = "작업장 정보를 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 작업장 목록을 조회를 합니다.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = WorkplaceDTO.class))))})
    @GetMapping("/workplaces/find")
    public List<WorkplaceDTO> getAllWorkplaces() {
        List<Workplace> workplaces = standardInformationService.getAllWorkplaces();
        return workplaces.stream()
                .map(workplace -> WorkplaceDTO.builder()
                        .workplaceId(workplace.getWorkplaceId())
                        .lineId(workplace.getLineId())
                        .workplaceName(workplace.getWorkplaceName())
                        .workplaceType(workplace.getWorkplaceType())
                        .workplaceCode(workplace.getWorkplaceCode())
                        .workplaceStatus(workplace.getWorkplaceStatus())
                        .workplaceLocation(workplace.getWorkplaceLocation())
                        .managerName(workplace.getManagerName())
                        .workplaceCapacity(workplace.getWorkplaceCapacity())
                        .build())
                .collect(Collectors.toList());
    }


    // 작업장 등록
    @Operation(summary = "작업장정보 등록", description = "새로운 작업장을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "작업장 등록 성공",
                    content = @Content(schema = @Schema(implementation = StandardInformationMessage.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")})
    @PostMapping("/workplaces/regist")
    public ResponseEntity<StandardInformationMessage> createWorkplace(
            @RequestBody @Parameter(description = "등록할 작업장 정보", required = true) WorkplaceDTO workplaceDTO) {
        Workplace savedWorkplace = standardInformationService.createWorkplace(workplaceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardInformationMessage(HttpStatus.CREATED.value(),
                        "등록에 성공했어!! 축하해! ID :" + savedWorkplace.getWorkplaceId()));
    }


    // 작업장 수정
    @Operation(summary = "작업장정보 수정", description = "장업장 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업장 수정 성공!",
                    content = @Content(schema = @Schema(implementation = StandardInformationMessage.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID의 작업장을 찾을수없어요")})
    @PutMapping("/workplaces/{id}")
    public ResponseEntity<StandardInformationMessage> updateWorkplace(
            @PathVariable @Parameter(description = "수정할 작업장의 ID", required = true) String id,
            @RequestBody @Parameter(description = "수정할 작업장 정보", required = true) WorkplaceDTO workplaceDTO) {
        Workplace updateWorkplace = standardInformationService.updateWorkplace(id, workplaceDTO);
        return ResponseEntity.ok(new StandardInformationMessage(HttpStatus.OK.value(),
                "수정이 완료되었어요 짝짝!!" + updateWorkplace.getWorkplaceId()));
    }


    // 작업장 등록 삭제
    @Operation(summary = "작업장정보 삭제", description = "작업장정보를 삭제 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업장 삭제 성공",
                    content = @Content(schema = @Schema(implementation = StandardInformationMessage.class))),
            @ApiResponse(responseCode = "404", description = "해당 ID의 작업장을 찾을 수 없음")})
    @DeleteMapping("/workplaces/{id}")
    public ResponseEntity<StandardInformationMessage> deleteWorkplace(
            @PathVariable @Parameter(description = "삭제할 장업장의 ID", required = true) String id) {
        try {
            standardInformationService.deleteWorkplace(id);
            return ResponseEntity.ok(new StandardInformationMessage(HttpStatus.OK.value(),
                    "삭제 성공! ID: " + id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StandardInformationMessage(HttpStatus.NOT_FOUND.value(),
                            "삭제 실패! 해당 ID의 작업장이 존재하지 않습니다."));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new StandardInformationMessage(HttpStatus.CONFLICT.value(),
                            "삭제 실패! 연관된 데이터가 존재합니다."));
        }
    }
}













