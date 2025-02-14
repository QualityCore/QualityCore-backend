package com.org.qualitycore.productionPlan.model.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ProductionPlanDTO {
    private LocalDate planYm;      //계획년월
    private String productId;   // 제품코드
    private String productName; // 제품명
    private String sizeSpec;    // 규격
    private Integer planQty;    // 계획수량
    private String status;      // 상태

}
