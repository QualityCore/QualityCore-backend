package com.org.qualitycore.masterdata.model.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MasterDataMessage {

    private  int httpStatusCode; // http 상세코드
    private  String message; // 메시지
}
