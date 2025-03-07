package com.org.qualitycore.standardinformation.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Schema(description = "여과 공정 DTO")
public class FiltrationProcessDTO {

    @Schema(description = "여과 공정 ID", example = "FI001")
    private String filtrationId;

    @Schema(description = "작업지시 ID", example = "LOT2025021201")
    private String lotNo;

    @Schema(description = "여과 소요 시간 (분)", example = "50")
    private Integer filtrationTime;

    @Schema(description = "곡물 흡수량 (L)", example = "360.0")
    private Double grainAbsorption;

    @Schema(description = "회수된 워트량 (L)", example = "3100.0")
    private Double recoveredWortVolume;

    @Schema(description = "손실량 (L)", example = "10.0")
    private Double lossVolume;

    @Schema(description = "메모 사항", example = "작업자: 강동원, 여과 완료")
    private String notes;

    @Schema(description = "공정 추적 DTO" )
    private ProcessTrackingDTONam processTracking;

}
