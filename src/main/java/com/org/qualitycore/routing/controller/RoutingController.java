package com.org.qualitycore.routing.controller;

import com.org.qualitycore.common.Message;
import com.org.qualitycore.routing.model.dto.ProcessTrackingDTO;
import com.org.qualitycore.routing.model.service.RoutingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Routing",description = "라우팅 API_Controller")
public class RoutingController {

    private final RoutingService routingService;

    @GetMapping("/processTracking")
    public ResponseEntity<Message> getProcessTracking(
            @RequestParam(required = false) String lotNo,
            @RequestParam(required = false) String processStatus
    ) {
        try {
            List<ProcessTrackingDTO> trackingList = routingService.findProcessTracking(lotNo, processStatus);
            return ResponseEntity.ok(
                    new Message(200, "공정 현황 조회 성공",
                            Map.of("trackingList", trackingList))
            );
        } catch (Exception e) {
            // 예외 로깅
            log.error("공정 현황 조회 중 오류 발생", e);

            // 상세 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Message(500, "공정 현황 조회 중 오류가 발생했습니다.",
                            Map.of("errorMessage", e.getMessage())));
        }
    }
}
