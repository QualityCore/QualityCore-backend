package com.org.qualitycore.masterdata.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "WORKPLACE_SCHEDULE")
public class WorkplaceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "schedule_seq")
    @SequenceGenerator(name = "schedule_seq", sequenceName = "SCHEDULE_ID", allocationSize = 1)
    @Column(name = "SCHEDULE_ID")
    private Long scheduleID; // 스케줄 고유 id

    @ManyToOne
    @JoinColumn(name ="WORKPLACE_CODE" , referencedColumnName = "WORKPLACE_CODE" , nullable = false)
    private Workplace workplace; // 부모(작업장등록) 엔티티와 관계설정


    @Column(name = "SHIFT_TYPE" ,nullable = false)
    private String shiftType; // 작업조 선택

    @Column(name = "START_TIME" , nullable = false)
    private LocalDateTime startTime; //작업시작시간

    @Column(name="END_TIME" , nullable = false)
    private LocalDateTime endTime; //작업종료시간

    @Column(name = "CREATED_AT", nullable = false)
    private  LocalDateTime createdAt; // 생성날짜

    @Column(name = "UPDATED_AT",nullable = false)
    private LocalDateTime updatedAt; // 수정날짜
}
