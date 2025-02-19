package com.org.qualitycore.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "EMP_SCHEDULE")
@Entity
public class Attendance {

    @Id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId; // 근태코드

    @Column(name = "CHECK_IN")
    private LocalDateTime checkIn; // 근무시간

    @Column(name = "CHECK_OUT")
    private LocalDateTime checkOut; // 근무종료시간

    @Column(name = "TOTAL_TIME")
    private LocalDateTime totalTime; // 총 근무시간

    @Column(name = "WORKING_TIME")
    private LocalDateTime workingTime; // 현재 근무시간

    @Column(name = "WORK_STATUS")
    private String workStatus; // 근무상태

    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    private Employee employee;

}
