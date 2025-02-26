package com.org.qualitycore.standardinformation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "분쇄 DTO")
public class MaterialGrindingDTO {

    @Schema(description = "분쇄공정 ID" , example ="GR001")
    private String grindingId;

    @Schema(description = "작업지시 ID" , example ="LOT2025021301")
    private String lotNo;

    @Schema(description = "상태 코드 ID", example = "SC001")
    private String statusCode;

    @Schema(description = "주원료" , example ="쌀")
    private String mainMaterial;

    @Schema(description ="주원료 투입량" , example ="50.00")
    private Double mainMaterialInputVolume;

    @Schema(description = "맥아종류" , example ="필스너 몰트")
    private String maltType;

    @Schema(description = "맥아 투입량" , example ="450")
    private Double maltInputVolume;

    @Schema(description = "분쇄 간격 설정 " , example ="1.00")
    private double grindIntervalSetting;

    @Schema(description = "분쇄 속도 설정" , example = "150.00")
    private double grindSpeedSetting;

    @Schema(description = "소요시간" , example = "40")
    private Integer grindDuration;

    @Schema(description = "공정 상태", example = "대기중")
    private String processStatus;

    @Schema(description = "메모사항" , example = "작업자 : 강동원 작업완료")
    private String notes;

    @Schema(description = "시작시간" , example ="2025-02-12T10:15:30" )
    private LocalDateTime startTime;

    @Schema(description = "예상 종료 시간" , example = "2025-02-12T10:55:30")
    private LocalDateTime expectedEndTime;

    @Schema(description = "실제 종료 시간" , example = "2025-02-12T11:00:30")
    private LocalDateTime actualEndTime;
}
