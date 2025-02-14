package com.org.qualitycore.productionPlan.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PLAN_MST")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlanMst {

    @Id
    @Column(name= "PLAN_ID")
    private String planId;

    @Column(name = "PLAN_YM", nullable = false)
    private LocalDate planYm;

    @Column(name="STATUS", nullable = false)
    private String status;
}
