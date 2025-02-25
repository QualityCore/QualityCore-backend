package com.org.qualitycore.work.model.entity;

import jakarta.persistence.*;
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
    private String lineMaterialId; // String 타입으로 UUID 사용 가능

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "MATERIAL_TYPE")
    private String materialType;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REQUIRED_QTY_PER_UNIT")
    private Integer requiredQtyPerUnit;

    @Column(name = "PRICE_PER_UNIT")
    private Integer pricePerUnit;

    @Column(name = "TOTAL_COST")
    private Integer totalCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_NO")
    private WorkOrders workOrders;


}

