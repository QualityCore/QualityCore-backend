package com.org.qualitycore.work.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "workOrders")
@Entity
@Table(name = "LINE_MATERIAL")
@Builder
public class LineMaterial {

    @Id
    @Column(name = "LINE_MATERIAL_ID")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String lineMaterialId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "TOTAL_QTY")
    private String totalQty;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REQUIRED_QTY_PER_UNIT")
    private double requiredQtyPerUnit;

    @Column(name = "PROCESS_STEP")
    private String processStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_NO")
    private WorkOrders workOrders;

    // 남규  LOT_NO 추가함.
    @Column(name = "LOT_NO" , nullable = false, updatable = false ,insertable = false)
    @Schema(description = "작업지시 ID" , example = "LOT2025021201")
    private String lotNo;




}

