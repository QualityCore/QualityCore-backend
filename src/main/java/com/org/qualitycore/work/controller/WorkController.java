package com.org.qualitycore.work.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.dto.WorkLotDTO;
import com.org.qualitycore.work.model.service.WorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "WorkOrder(작업지시서)", description = "작업지시서 API_Controller")
public class WorkController {

    private final WorkService workService;

     // 작업지시서 전체 조회
    @GetMapping("/work")
    @Operation(summary = "작업지시서 전체 조회", description = "작업지시서 메인화면에서 모든 작업지시서를 조회하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작업지시서 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "작업지시서가 없습니다.")})
    public ResponseEntity<Message> findAllWorkOrders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 작업지시서 전체 조회
        List<WorkFindAllDTO> work = workService.findAllWorkOrders();

        // 전체조회할 데이터가 없을경우 예외처리
        if (work.isEmpty()) {

            Map<String, Object> res = new HashMap<>();

            res.put("status", 404);

            res.put("message", "작업지시서가 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new Message(404, "작업지시서가 없습니다.", res));
        }

        // 전체 조회 성공 시
        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "작업지시서 전체조회 성공", res));
    }

    // 작업지시서 상세 조회
    @Operation(summary = "작업지시서 상세 조회",
            description = "작업지시서 번호를 통해 작업지시서 상세 조회를 합니다.",
            parameters = {@Parameter(name = "workOrderId", description = "상세조회를 위한 작업지시서 고유 PK")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 상세조회 성공"),
            @ApiResponse(responseCode = "404", description = "작업지시서를 찾을 수 없습니다.")})
    @GetMapping("/work/{lotNo}")
    public ResponseEntity<Message> findByWorkOrderCode(@PathVariable("lotNo") String lotNo) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        WorkLotDTO work = null;

        try {
            work = workService.findByWorkOrderCode(lotNo);

        } catch (IllegalArgumentException e) {
            // 작업지시서를 찾을 수 없을 경우 404 응답
            Map<String, Object> res = new HashMap<>();

            res.put("status", 404);

            res.put("message", "작업지시서를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new Message(404, "작업지시서를 찾을 수 없습니다.", res));
        }

        // 작업지시서가 있을 경우 상세 조회
        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new Message(200, "작업지시서 상세조회 성공", res));
    }
//
//    // 작업지시서 생성
//    @PostMapping("/create")
//    @Operation(summary = "작업지시서 생성", description = "새로운 생산계획이 나오면 작업지시서를 등록합니다.",
//               parameters = {@Parameter(name = "work", description = "작업지시서에 대한 등록 정보가 담긴 DTO")})
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "작업지시서 생성 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")})
//    public ResponseEntity<?> workOrderCreate(@RequestBody WorkDTO work) {
//        try {
//            // 작업지시서 생성
//            workService.workOrderCreate(work);
//
//            // 작업지시서 생성 완료 시
//            Map<String, Object> res = new HashMap<>();
//
//            res.put("status", 201);
//
//            res.put("message", "작업지시서 생성 성공");
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(res);
//
//        } catch (IllegalArgumentException e) {
//            // 잘못된 요청 데이터를 보낼경우 예외처리
//
//            Map<String, Object> res = new HashMap<>();
//
//            res.put("status", 400);
//
//            res.put("message", "잘못된 요청 데이터");
//
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//        }
//    }
//
//    // 작업지시서 삭제
//    @DeleteMapping("/detail/{workOrderId}/delete")
//    @Operation(summary = "작업지시서 삭제", description = "작업지시서 번호를 통해 해당 작업지시서를 삭제합니다.",
//               parameters = {@Parameter(name = "workOrderId", description = "삭제할 작업지시서에 대한 고유 PK")})
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "작업지시서 삭제 성공"),
//            @ApiResponse(responseCode = "404", description = "작업지시서를 찾을 수 없습니다.")})
//    public ResponseEntity<?> workOrderDelete(@PathVariable("workOrderId") int workId) {
//
//        try {
//            workService.workOrderDelete(workId);
//
//            // 삭제 성공 시
//            Map<String, Object> res = new HashMap<>();
//
//            res.put("status", 200);
//
//            res.put("message", "작업지시서 삭제 성공");
//
//            return ResponseEntity.status(HttpStatus.OK).body(res);
//
//        } catch (ResourceNotFoundException e) {
//            // 삭제하려는 작업지시서가 없을 경우 예외 처리
//            Map<String, Object> res = new HashMap<>();
//
//            res.put("status", 404);
//
//            res.put("message", "작업지시서를 찾을 수 없습니다.");
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
//        }
//    }
}
