package com.org.qualitycore.standardinformation.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EquipmentInfoDTO {

    private String equipmentId;

    private String workplaceId;

    private String equipmentName;

    private String modelName;

    private String manufacturer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String installDate;

    private String equipmentStatus;

    private String equipmentImage;

    private String equipmentEtc;

    private String workplaceName;

    private String workplaceType;

}
