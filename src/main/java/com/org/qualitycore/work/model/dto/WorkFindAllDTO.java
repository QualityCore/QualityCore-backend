package com.org.qualitycore.work.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "작업지시서 전체 DTO")
@Builder(toBuilder = true)
public class WorkFindAllDTO {

    @Schema(description = "작업지시서 번호")
    private String lotNo;

    @Schema(description = "진행상태 PK")
    private String trackingId;

    @Schema(description = "진행률", example = "50%, 100%")
    private String workProgress;

    @Schema(description = "특이사항")
    private String workEtc;

    @Schema(description = "작업조")
    private String workTeam;

    @Schema(description = "생산제품")
    private String productName;

    @Schema(description = "진행상태")
    private String processStatus;

    @Schema(description = "공정명")
    private String processName;

    @Schema(description = "자재 목록")
    private List<LineMaterialDTO> lineMaterials;  // 추가된 필드

    @Schema(description = "생산라인 PK")
    private String  planLineId;

    @Schema(description = "직원 PK")
    private String empId;

    @Schema(description = "생산제품 PK")
    private String planProductId;

    @Schema(description = "생산계획 PK")
    private String planId;

}
