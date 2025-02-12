package com.org.qualitycore.masterdata.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "WORKPLACE_SCHEDULE")
@Schema(description = "작업장 가동시간 및 교대조 스케줄 엔티티")
public class WorkplaceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    @Schema(description = "스케줄 고유 ID", example = "1")
    private Long scheduleID; // 스케줄 고유 id

    @ManyToOne
    @JoinColumn(name ="WORKPLACE_CODE" , referencedColumnName = "WORKPLACE_CODE" , nullable = false)
    @Schema(description = "작업장이 참조하는 Workplace 엔티티")
    private Workplace workplace; // 부모(작업장등록) 엔티티와 관계설정


    @Column(name = "SHIFT_TYPE" ,nullable = false)
    @Schema(description = "작업조 유형 (예: 주간, 야간, 심야)", example = "주간")
    private String shiftType; // 작업조 선택

    @Column(name = "START_TIME" , nullable = false)
    @Schema(description = "작업 시작 시간", example = "2024-02-12T08:00:00")
    private LocalDateTime startTime; //작업시작시간

    @Column(name="END_TIME" , nullable = false)
    @Schema(description = "작업 종료 시간", example = "2024-02-12T17:00:00")
    private LocalDateTime endTime; //작업종료시간

    @CreationTimestamp // insert 시 자동으로 sysdate 값 저장
    @Column(name = "CREATED_AT", nullable = false)
    @Schema(description = "스케줄 생성 날짜", example = "2024-02-11T14:30:00")
    private  LocalDateTime createdAt; // 생성날짜

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    @Schema(description = "스케줄 수정 날짜", example = "2024-02-12T11:00:00")
    private LocalDateTime updatedAt; // 수정날짜
}
