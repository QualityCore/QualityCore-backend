package com.org.qualitycore.masterdata.model.entity;

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
public class WorkplaceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreationTimestamp // insert 시 자동으로 sysdate 값 저장
    @Column(name = "CREATED_AT", nullable = false)
    private  LocalDateTime createdAt; // 생성날짜

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt; // 수정날짜
}
