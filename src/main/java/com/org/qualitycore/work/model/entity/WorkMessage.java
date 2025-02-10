package com.org.qualitycore.work.model.entity;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkMessage {

    private int code; // 상태코드
    private String message; // 상태값
    private Map<String, Object> result; // 결과값


}
