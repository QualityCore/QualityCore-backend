package com.org.qualitycore.productionPlan.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "planMst", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanProduct> planProducts;
}
