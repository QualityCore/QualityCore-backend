package com.org.qualitycore.work.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "PLAN_PRODUCT")
public class PlanProduct {

    @Id
    @Column(name = "PLAN_PRODUCT_ID")
    private String planProductId;

    @Column(name = "PLAN_ID")
    private String planId;

    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PLAN_QTY")
    private int planQty;

    @Column(name = "BASE_TEMP")
    private int baseTemp;

    @Column(name = "STD_PROCESS_TIME")
    private int stdProcessTime;

    @Column(name = "FERMENT_TIME")
    private int fermentTime;

    @Column(name = "ALC_PERCENT")
    private int alcPercent;

    @Column(name = "FILTER_TEMP")
    private int filterTemp;

    @Column(name = "SIZE_SPEC")
    private String sizeSpec;

    @OneToMany(mappedBy = "planProduct", fetch = FetchType.LAZY)
    private List<WorkOrders> workOrders;
}
