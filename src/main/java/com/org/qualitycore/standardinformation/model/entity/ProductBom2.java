package com.org.qualitycore.standardinformation.model.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "ProductBomEntity")  // 엔티티명 지정
@Table(name = "PRODUCT_BOM")
public class ProductBom2 {

    @Id
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "BEER_TYPE")
    private String beerType;

    @Column(name = "SIZE_SPEC")
    private String sizeSpec;

    @Column(name = "ROOM_TEMPERATURE")
    private String roomTemperature;

    @Column(name = "STD_PROCESS_TIME")
    private String stdProcessTime;

    @Column(name = "FERMENT_TIME")
    private String fermentTime;

    @Column(name = "ALC_PERCENT")
    private float alcPercent;
}
