package com.org.qualitycore.standardinformation.model.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProcessInfoDTO {

    private String productId;
    private String processId;
    private String processName;
    private String processCode;
    private String processDescription;
    private String processType;
    private String beerType;
    private String processDuration;
    private String processStatus;
    private Date createdAt;
    private Date updatedAt;

}
