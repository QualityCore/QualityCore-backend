package com.org.qualitycore.standardinformation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Schema(description = "숙성 상세 공정 DTO")
public class MaturationDetailsDTO {

    @Schema(description = "숙성 공정 ID", example = "MAR001")
    private String maturationId;

    @Schema(description = "작업지시 ID", example = "LOT2025021201")
    private String lotNo;

    @Schema(description = "숙성 소요 시간 (분)", example = "1440")
    private Integer maturationTime;

    @Schema(description = "숙성 시작 온도 (°C)", example = "15.0")
    private Double startTemperature;

    @Schema(description = "메모 사항", example = "숙성 완료, 향미 안정적")
    private String notes;

    @Schema(description = "시작시간" , example = "2025-02-12T10:15:30")
    private LocalDateTime startTime;

    @Schema(description = "예상 종료 시간" , example = "2025-02-12T10:55:30")
    private LocalDateTime expectedEndTime;

    @Schema(description = "실제 종료 시간" , example = "2025-02-12T11:00:30")
    private LocalDateTime actualEndTime;

    @Schema(description = "공정 추적 DTO" )
    private ProcessTrackingDTONam processTracking;

}
