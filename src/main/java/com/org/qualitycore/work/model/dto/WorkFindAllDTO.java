package com.org.qualitycore.work.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "작업지시서 관련 DTO")
public class WorkFindAllDTO {

    private String lotNo;

    private String workProgress;

    private String workEtc;

    private String workTeam;

    private int lineNo;

    private int planQty;

    private String startDate;

    private String endDate;

    private String productName;

    private String sizeSpec;

    private String processStatus;
}
