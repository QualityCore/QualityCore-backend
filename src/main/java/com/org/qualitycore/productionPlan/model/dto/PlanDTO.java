package com.org.qualitycore.productionPlan.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PlanDTO {
    private Long planId;
    private String planYm;
    private String status;

}
