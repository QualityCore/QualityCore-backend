package com.org.qualitycore.work.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "LINE_MATERIAL")
public class LineMaterial {

    @Id
    @Column(name = "LINE_MATERIAL_ID")
    private String lineMaterialId;

    @Column(name = "PLAN_LINE_ID")
    private String planLineId;

    @Column(name = "PLAN_PRODUCT_ID")
    private String planProductId;

    @Column(name = "MATERIAL_ID")
    private String materialId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "MATERIAL_TYPE")
    private String materialType;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REQUIRED_QTY_PER_UNIT")
    private int requiredQtyPerUnit;

    @Column(name = "PRICE_PER_UNIT")
    private int pricePerUnit;

    @Column(name = "TOTAL_COST")
    private int totalCost;

    @Column(name = "LOT_NO")
    private String lotNo;

}

