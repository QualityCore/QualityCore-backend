package com.org.qualitycore.standardinformation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "끓임 DTO")
public class BoilingProcessDTO {

    @Schema(description = "끓임 공정 ID", example = "BO001")
    private String boilingId;

    @Schema(description = "작업지시 ID", example = "LOT2025021201")
    private String lotNo;

    @Schema(description = "끓임 소요 시간 (분)", example = "60.0")
    private Integer boilingTime;

    @Schema(description = "끓임 온도 (°C)", example = "100.0")
    private Double temperature;

    @Schema(description = "초기 워트량 (L)", example = "3200.0")
    private Double initialWortVolume;

    @Schema(description = "끓임 후 워트량 (L)", example = "3000.0")
    private Double postBoilWortVolume;

    @Schema(description = "끓임 손실량 (L)", example = "200.0")
    private Double boilLossVolume;

    @Schema(description = "첫번쨰 홉 이름" , example = "마그넘 홉")
    private String firstHopName ;

    @Schema(description = "첫번째 홉 투입량", example = "20.0")
    private Double firstHopAmount;

    @Schema(description = "두번쨰 홉 이름" , example = "캐스케이드 홉")
    private String secondHopName;

    @Schema(description = "두번쨰 홉 투입량" , example = "15.0")
    private Double secondHopAmount;

    @Schema(description = "메모 사항", example = "작업자: 홍길동, 끓임 완료")
    private String notes;

    @Schema(description = "공정 추적 DTO" )
    private ProcessTrackingDTONam processTracking;
}
