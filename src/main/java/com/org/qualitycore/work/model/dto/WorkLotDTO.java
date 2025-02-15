package com.org.qualitycore.work.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "작업지시서 상세조회 DTO")
public class WorkLotDTO {

    @Schema(description = "작업지시서 번호")
    private String lotNo;

    @Schema(description = "진행률", example = "50%, 100%")
    private String workProgress;

    @Schema(description = "특이사항")
    private String workEtc;

    @Schema(description = "작업조")
    private String workTeam;

    @Schema(description = "생산라인번호")
    private int lineNo;

    @Schema(description = "생산계획수량")
    private int planQty;

    @Schema(description = "생산시작일")
    private Date startDate;

    @Schema(description = "생산종료일")
    private Date endDate;

    @Schema(description = "생산제품")
    private String productName;

    @Schema(description = "규격")
    private String sizeSpec;

    @Schema(description = "현재 공정")
    private String processStatus;
}
