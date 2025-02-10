package com.org.qualitycore.productionPlan.model.entity;

import lombok.*;

import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PlanMessage {

    private int code; // 상태코드
    private String message; // 상태
    private Map<String, Object> result; // 결과값
}
