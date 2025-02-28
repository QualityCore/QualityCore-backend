package com.org.qualitycore.work.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PlanLineDTO {

    private String planLineId;

    @Schema(description = "생산라인번호")
    private int lineNo;

    @Schema(description = "생산계획수량")
    private int planQty;

    @Schema(description = "생산시작일")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Schema(description = "생산종료일")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

}
