package com.org.qualitycore.work.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import oracle.sql.DATE;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkDTO {

    private int workOrderId; // 작업지시서 번호
    private int bomId; // bom 번호
    private String workTeam; // 작업조 ex)A조
    private String workTitle; // 제품명 ex) 카리나맥주
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workStartDate; // 생산시작일
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workEndDate; // 생산종료일
    private String workQuantity; // 생산수량 ex) 300개
    private String workProcess; // 생산공정 ex) 여과, 당화
    private String workLine; // 생산라인 ex) 1LINE, 2LINE
    private String workOrderStatus; // 생산여부 ex) 진행중, 대기중, 완료
    private String workCapacity; // 생산용량 ex) 500ml, 1L
    private String workProgress; // 진행률 ex) 10%, 20%
    private String workEtc; // 특이사항
}
