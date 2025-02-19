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
    private String status = "미확정";

    @Column(name = "CREATED_BY", nullable = false)
    private String createdBy; // 추가됨

    @OneToMany(mappedBy = "planMst", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanProduct> planProducts;

    // 💡 INSERT 전에 자동으로 기본값 설정
    @PrePersist
    public void prePersist() {
        if (this.createdBy == null) {
            this.createdBy = "SYSTEM";  // 기본값 설정
        }
    }

}
