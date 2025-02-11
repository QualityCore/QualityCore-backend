package com.org.qualitycore.work.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "API 를 보낼때 쓰는 상태코드, 상태값, 결과값 담는 DTO")
public class WorkMessage {

    private int status; // 상태코드
    private String message; // 메시지
    private Map<String, Object> result; // 결과값
}
