package com.org.qualitycore.standardinformation.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "MASHING_PROCESS")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "당화공정 엔티티")
public class MashingProcess {

    @Id
    @Column(name = "MASHING_ID", nullable = false , updatable = false)
    @Schema(description = "당화공정 ID" , example = "MA001")
    private String mashingId;


    @Column(name = "LOT_NO" , nullable = false, updatable = false ,insertable = false)
    @Schema(description = "작업지시 ID" , example = "LOT2025021201")
    private String lotNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_NO" , referencedColumnName = "LOT_NO", nullable = false)
    @Schema(description = "작업지시 ID" , example ="LOT2025021301")
    private  WorkOrder workOrder;


    @Column(name ="STATUS_CODE" , nullable = false , updatable = false )
    @Schema(description = "상태 코드 ID", example = "SC002")
    private String statusCode;


    @Column(name ="MASHING_TIME",nullable = false)
    @Schema(description = "당화 소요 시간" , example = "50")
    private Integer mashingTime;


    @Column(name ="TEMPERATURE" , nullable = false )
    @Schema(description = "온도", example = "65")
    private String temperature;


    @Column(name ="PH_VALUE")
    @Schema(description = "pH값" , example = "5.40")
    private Double phValue;


    @Column(name ="GRAIN_RATIO" , nullable = false)
    @Schema(description = "곡물 비율", example = "1")
    private Integer grainRatio;


    @Column(name ="WATER_RATIO",nullable = false)
    @Schema(description = "물 비율" , example = "4")
    private Integer waterRatio;


    @Column(name ="WATER_INPUT_VOLUME" , nullable = false )
    @Schema(description = "물 투입량", example = "3200")
    private Integer waterInputVolume;


    @Column(name = "PROCESS_STATUS", nullable = false)
    @Schema(description = "공정 상태", example = "대기중")
    private String processStatus;

    @Column(name = "NOTES")
    @Schema(description = "메모사항" , example = "작업자 : 강동원  작업완료" )
    private String notes;


    @CreationTimestamp // insert 시 자동으로 sysdate 값 저장
    @Column(name = "START_TIME" , nullable = false , updatable = false)
    @Schema(description = "시작시간" , example = "2025-02-12T10:15:30")
    private LocalDateTime startTime;

    @Column(name = "EXPECTED_END_TIME" , nullable = false)
    @Schema(description = "예상 종료 시간" , example = "2025-02-12T10:55:30")
    private LocalDateTime expectedEndTime;


    @PreUpdate   // update 이전 자동 실행
    public void updateExpectedEndTime() {
        if (expectedEndTime == null && mashingTime != null) {
            expectedEndTime = startTime.plusMinutes(mashingTime);
        }
    }

    @UpdateTimestamp
    @Column(name = "ACTUAL_END_TIME")
    @Schema(description = "실제 종료 시간" , example = "2025-02-12T11:00:30")
    private LocalDateTime actualEndTime;


    @PrePersist
    public void prePersist() {
        // 시작 시간이 없으면 현재 시간으로 설정
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }

        // 소요 시간이 있고, 예상 종료 시간이 설정되지 않았다면 계산
        if (expectedEndTime == null && mashingTime != null) {
            expectedEndTime = startTime.plusMinutes(mashingTime);
        }

        // 공정 상태 기본값 설정
        if (processStatus == null) {
            processStatus = "대기중";
        }

        // 상태 코드 기본값 설정
        if (statusCode == null) {
            statusCode = "SC002";
        }
    }


    @Override
    public String toString() {
        return "MashingProcess{" +
                "mashingId='" + mashingId + '\'' +
                ", lotNo='" + lotNo + '\'' +
                ", workOrder=" + workOrder +
                ", statusCode='" + statusCode + '\'' +
                ", mashingTime=" + mashingTime +
                ", temperature='" + temperature + '\'' +
                ", phValue=" + phValue +
                ", grainRatio=" + grainRatio +
                ", waterRatio=" + waterRatio +
                ", waterInputVolume=" + waterInputVolume +
                ", processStatus='" + processStatus + '\'' +
                ", notes='" + notes + '\'' +
                ", startTime=" + startTime +
                ", expectedEndTime=" + expectedEndTime +
                ", actualEndTime=" + actualEndTime +
                '}';
    }
}
