package com.org.qualitycore.work.controller;

import com.org.qualitycore.exception.ResourceNotFoundException;
import com.org.qualitycore.work.model.dto.WorkDTO;
import com.org.qualitycore.work.model.entity.WorkMessage;
import com.org.qualitycore.work.model.service.WorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/work")
@RequiredArgsConstructor
@Tag(name = "WorkOrder", description = "작업지시서 API_Controller")
public class WorkController {

    private final WorkService workService;

    // 작업지시서 전체 조회
    @GetMapping("/list")
    @Operation(summary = "작업지시서 전체 조회", description = "작업지시서 메인화면에서 모든 작업지시서를 조회하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작업지시서 전체조회 성공"),
            @ApiResponse(responseCode = "404", description = "작업지시서가 없습니다.")})
    public ResponseEntity<WorkMessage> findAllWorkOrders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 작업지시서 전체 조회
        List<WorkDTO> work = workService.findAllWorkOrders();

        // 전체조회할 데이터가 없을경우 예외처리
        if (work.isEmpty()) {

            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "작업지시서가 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new WorkMessage(404, "작업지시서가 없습니다.", response));
        }

        // 전체 조회 성공 시
        Map<String, Object> res = new HashMap<>();
        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new WorkMessage(200, "작업지시서 전체조회 성공", res));
    }



    // 작업지시서 상세 조회
    @Operation(summary = "작업지시서 상세 조회",
            description = "작업지시서 번호를 통해 작업지시서 상세 조회를 합니다. " +
                          "@PathVariable 에서 받은 workOrderId 는 작업지시서 번호이며 " +
                          "타입은 int 타입이고 번호가 자동증가하게 만들었습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 상세조회 성공"),
            @ApiResponse(responseCode = "404", description = "작업지시서를 찾을 수 없습니다.")})
    @GetMapping("/detail/{workOrderId}")
    public ResponseEntity<WorkMessage> findByWorkOrderCode(@PathVariable("workOrderId") int workId) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        WorkDTO work = null;

        try {
            work = workService.findByWorkOrderCode(workId);

        } catch (IllegalArgumentException e) {
            // 작업지시서를 찾을 수 없을 경우 404 응답
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "작업지시서를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new WorkMessage(404, "작업지시서를 찾을 수 없습니다.", response));
        }

        // 작업지시서가 있을 경우 상세 조회
        Map<String, Object> res = new HashMap<>();

        res.put("work", work);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new WorkMessage(200, "작업지시서 상세조회 성공", res));
    }



    // 작업지시서 생성
    @PostMapping("/create")
    @Operation(summary = "작업지시서 생성", description = "새로운 생산계획이 나오면 작업지시서를 등록합니다. " +
                                                       "@RequestBody 로 작업지시서 정보들이 담겨있는 WorkDTO 를 변수로 담아서 " +
                                                       "작업지시서를 생성 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "작업지시서 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")})
    public ResponseEntity<?> workOrderCreate(@RequestBody WorkDTO work) {
        try {
            // 작업지시서 생성
            workService.workOrderCreate(work);

            // 작업지시서 생성 완료 시
            Map<String, Object> response = new HashMap<>();
            response.put("status", 201);
            response.put("message", "작업지시서 생성 성공");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // 잘못된 요청 데이터를 보낼경우 예외처리
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "잘못된 요청 데이터");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    // 작업지시서 수정
    @PutMapping("/update")
    @Operation(summary = "작업지시서 수정", description = "기존 작업지시서를 수정을하여 작업지시서에 변경이 있을시 수정을 할 수 있도록 합니다. 또한 " +
                                                       "RequestBody 로 작업지시서 정보가 담겨있는 WorkDTO 를 변수로 담아서" +
                                                       " 제가 필요한것만 Builder 로 업데이트 하여 수정을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작업지시서 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")})
    public ResponseEntity<?> workOrderUpdate(@RequestBody WorkDTO work) {
        try {
            // 작업지시서 수정
            workService.workOrderUpdate(work);

            // 성공적인 응답
            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "작업지시서 수정 성공");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (IllegalArgumentException e) {
            // 잘못된 요청 데이터 (예: 수정할 작업지시서가 존재하지 않음)
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "작업지시서를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    // 작업지시서 삭제
    @DeleteMapping("/detail/delete/{workOrderId}")
    @Operation(summary = "작업지시서 삭제", description = "작업지시서 번호를 통해 해당 작업지시서를 삭제합니다. " +
                                                       "@PathVariable 로 workOrderId 즉 작업지시 번호를 매개변수로 받아서 " +
                                                       "저희가 레파지토리에 있는 deleteById 를 이용해 작업지시 번호로 삭제를 하게 하는 기능입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작업지시서 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "작업지시서를 찾을 수 없습니다.")
    })
    public ResponseEntity<?> workOrderDelete(@PathVariable("workOrderId") int workId) {

        try {
            workService.workOrderDelete(workId);

            // 삭제 성공 시
            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "작업지시서 삭제 성공");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResourceNotFoundException e) {
            // 삭제하려는 작업지시서가 없을 경우 예외 처리
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "작업지시서를 찾을 수 없습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
