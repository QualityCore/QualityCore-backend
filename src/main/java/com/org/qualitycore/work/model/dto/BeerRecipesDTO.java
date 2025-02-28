package com.org.qualitycore.work.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BeerRecipesDTO {

    private String id;
    private String beerName;
    private String materialId;
    private String quantity;
    private String processStep;
    private String materialName;
    private String materialType;

}
