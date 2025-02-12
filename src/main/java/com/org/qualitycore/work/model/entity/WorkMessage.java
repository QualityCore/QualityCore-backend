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

    @Schema(description = "상태 코드", example = "200,404,500 등등..")
    private int status; // 상태코드

    @Schema(description = "상태 값", example = "메뉴조회 성공!!")
    private String message; // 메시지

    @Schema(description = "결과 값")
    private Map<String, Object> result; // 결과값
}
