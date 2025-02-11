package com.org.qualitycore.work.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "작업지시서 관련 DTO")
public class WorkDTO {

    @Schema(description = "작업지시서 번호(PK)")
    private int workOrderId;
    @Schema(description = "BOM 번호(FK)")
    private int bomId;
    @Schema(description = "작업조", example = "A조, B조")
    private String workTeam;
    @Schema(description = "제품명", example = "카리나맥주")
    private String workTitle;
    @Schema(description = "생산시작일")
    @JsonFormat(pattern = "yyyy-MM-dd") // 날짜 변환
    private Date workStartDate;
    @Schema(description = "생산종료일")
    @JsonFormat(pattern = "yyyy-MM-dd") // 날짜 변환
    private Date workEndDate;
    @Schema(description = "생산수량", example = "300개")
    private String workQuantity;
    @Schema(description = "생산공정", example = "여과, 당화")
    private String workProcess;
    @Schema(description = "생산라인", example = "1라인, 2라인")
    private String workLine;
    @Schema(description = "생산여부", example = "진행중, 대기중, 완료")
    private String workOrderStatus;
    @Schema(description = "생산용량", example = "500mL, 1L")
    private String workCapacity;
    @Schema(description = "진행율", example = "10%, 20%")
    private String workProgress;
    @Schema(description = "특이사항")
    private String workEtc;
}
