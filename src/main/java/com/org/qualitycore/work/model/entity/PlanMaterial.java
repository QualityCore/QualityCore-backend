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
@Table(name = "PLAN_MATERIAL")
public class PlanMaterial {

    @Id
    @Column(name = "PLAN_MATERIAL_ID")
    private String planMaterialId;

    @Column(name = "PLAN_PRODUCT_ID")
    private String planProductId;

    @Column(name = "MATERIAL_ID")
    private String materialId;

    @Column(name = "MATERIAL_TYPE")
    private String materialType;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "STD_QTY")
    private String stdQty;

    @Column(name = "PLAN_QTY")
    private String planQty;

    @Column(name = "STOCK_QTY")
    private String stockQty;

    @Column(name = "STATUS")
    private String status;
}
