package com.org.qualitycore.standardinformation.model.dto;

import jakarta.persistence.Column;

import java.util.Date;

public class LabelInfoDTO {

//  label_info 테이블
    private String labelId;
    private String productId;
    private String brandName;
    private Date productionDate;
    private String labelImage;
    private String beerSupplier;
// product_bom 테이블
    private String productName;
    private String sizeSpec;
    private float alcPercent;
}
