package com.org.qualitycore.work.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
@Table(name = "WORK_ORDER")
@Entity
@Schema(description = "작업지시서 관련 Entity")
public class WorkOrder {


    @Id
    @Column(name = "LOT_NO")
    private String lotNO; // 작업지시 번호(PK)

    @Column(name = "PLAN_LINE_ID")
    private int planLineId; // 생산라인 ID(FK)

    @Column(name = "PLAN_PRODUCT_ID") // 제품 ID(FK)
    private String planProductId;

    @Column(name = "EMP_ID")
    private String empId; // 사원번호(FK)

    @Column(name = "WORK_PROGRESS")
    private Date workProgress; // 진행률

    @Column(name = "WORK_ETC")
    private Date workEtc; // 특이사항

}
