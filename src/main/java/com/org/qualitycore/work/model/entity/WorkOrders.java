package com.org.qualitycore.work.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
@Table(name = "WORK_ORDER")
@Entity
@Schema(description = "작업지시서 관련 Entity")
public class WorkOrders {

    @Id
    @Column(name = "LOT_NO")
    private String lotNo; // 작업지시 번호(PK)

    @ManyToOne
    @JoinColumn(name = "PLAN_LINE_ID")
    private PlanLine planLine; // 생산라인 엔티티

    @ManyToOne
    @JoinColumn(name = "PLAN_PRODUCT_ID")
    private PlanProduct planProduct; // 생산제품 엔티티

    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    private Employee employee; // 사원 엔티티

    @ManyToOne
    @JoinColumn(name = "STATUS_CODE")
    private ProgressStatus progressStatus; // 현재 공정 진행상태 엔티티

    @Column(name = "WORK_PROGRESS")
    private String workProgress; // 진행률

    @Column(name = "WORK_ETC")
    private String workEtc; // 특이사항

}
