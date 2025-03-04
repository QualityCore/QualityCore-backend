package com.org.qualitycore.standardinformation.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EquipmentInfoDTO {

    private String equipmentId;
    private String workPlaceId;
    private String equipmentName;
    private String equipmentCode;
    private String modelName;
    private String manufacturer;
    private String installationDate;
    private String equipmentStatus;
    private String createdAt;
    private String updatedAt;
}
