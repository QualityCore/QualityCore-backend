package com.org.qualitycore.masterdata.model.dto;

import com.org.qualitycore.masterdata.model.entity.Workplace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "작업장 가동시간 및 교대조 스케줄 설정 DTO")
public class WorkplaceScheduleDTO {

    @Schema(description = "스케줄 고유 ID", example = "1")
    private Long scheduleID;

    @Schema(description = "연관된 작업장 정보 (Workplace 엔티티)")
    private Workplace workplace;

    @Schema(description = "작업조 유형 (예: 주간, 야간, 심야)", example = "주간")
    private String shiftType;

    @Schema(description = "작업 시작 시간", example = "2024-02-12T08:00:00")
    private LocalDateTime startTime;

    @Schema(description = "작업 종료 시간", example = "2024-02-12T17:00:00")
    private LocalDateTime endTime;

    @Schema(description = "생성 날짜", example = "2024-02-11T14:30:00")
    private LocalDateTime createdAt;
}