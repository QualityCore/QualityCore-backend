package com.org.qualitycore.productionPlan.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ProductionPlanProduct")
@Table(name = "PLAN_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PlanProduct{

    // 이곳은 생산제품 정보를 저장하는 테이블입니당.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAN_PRODUCT_ID")
    private String planProductId; // 생산계획제품ID

    @ManyToOne
    @JoinColumn(name = "PLAN_ID", nullable = false)
    private PlanMst planMst;    //생산계획 연결

    @Column(name = "PRODUCT_ID", nullable = false)
    private String productId;   //제품코드

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;   //제품명

    @Column(name = "PLAN_QTY", nullable = false)
    private Integer planQty;      //계획수량

}
