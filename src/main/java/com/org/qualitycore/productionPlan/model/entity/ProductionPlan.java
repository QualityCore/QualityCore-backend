package com.org.qualitycore.productionPlan.model.entity;


import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;


@Entity
@Table(name = "PLAN_MST")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ProductionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false)
    private String planYm;

    @Column(nullable = false)
    private String status;
}
