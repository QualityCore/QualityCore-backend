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
@Table(name = "TBL_WORK")
@Entity
@Schema(description = "작업지시서 관련 Entity")
public class Work {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "work_order_id_seq",
            sequenceName = "work_order_id_seq",
            allocationSize = 1
    )
    @Column(name = "WORK_ORDER_ID")
    private int workOrderId; // 작업지시서 번호

    @Column(name = "BOM_ID")
    private int bomId; // bom 번호

    @Column(name = "WORK_TEAM")
    private String workTeam; // 작업조 ex)A조

    @Column(name = "WORK_TITLE")
    private String workTitle; // 제품명 ex) 카리나맥주

    @Column(name = "WORK_START_DATE")
    private Date workStartDate; // 생산시작일

    @Column(name = "WORK_END_DATE")
    private Date workEndDate; // 생산종료일

    @Column(name = "WORK_QUANTITY")
    private String workQuantity; // 생산수량 ex) 300개

    @Column(name = "WORK_PROCESS")
    private String workProcess; // 생산공정 ex) 여과, 당화

    @Column(name = "WORK_LINE")
    private String workLine; // 생산라인 ex) 1LINE, 2LINE

    @Column(name = "WORK_ORDER_STATUS")
    private String workOrderStatus; // 생산여부 ex) 진행중, 대기중, 완료

    @Column(name = "WORK_CAPACITY")
    private String workCapacity; // 생산용량 ex) 500ml, 1L

    @Column(name = "WORK_PROGRESS")
    private String workProgress; // 진행률 ex) 10%, 20%

    @Column(name = "WORK_ETC")
    private String workEtc; // 특이사항
}
