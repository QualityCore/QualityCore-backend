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
    private int scheduleId; // 근태코드

    @Column(name = "CHECK_IN")
    private LocalDateTime checkIn; // 출근시간

    @Column(name = "CHECK_OUT")
    private LocalDateTime checkOut; // 출근

    @Column(name = "TOTAL_TIME")
    private LocalDateTime totalTime; // 총 근무시간

    @Column(name = "WORKING_TIME")
    private LocalDateTime workingTime; // 현재 근무시간

    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    private Employee employee;

}
