package com.org.qualitycore.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "TBL_ATTENDANCE")
@Entity
public class Attendance {

    @Id
    @Column(name = "ATTENDANCE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_id_seq_gen")
    @SequenceGenerator(
            name = "attendance_id_seq_gen",
            sequenceName = "attendance_id_seq",  // 오라클 시퀀스 이름
            allocationSize = 1  // 한 번에 하나씩 증가
    )
    private int attendanceId; // 근태코드

    @Column(name = "WORKER")
    private String worker; // 근무자

    @Column(name = "CHECK_IN")
    private LocalDateTime checkIn; // 출근

    @Column(name = "CHECK_OUT")
    private LocalDateTime checkOut; // 퇴근

    @Column(name = "TOTAL_TIME")
    private LocalDateTime totalTime; // 총 근무시간

    @Column(name = "WORKING_TIME")
    private LocalDateTime workingTime; // 현재 근무시간

    @Column(name = "CHANGE_CHECK_IN")
    private LocalDateTime changeCheckIn; // 변경된 출근 시간

    @Column(name = "CHANGE_CHECK_OUT")
    private LocalDateTime changeCheckOut; // 변경된 퇴근 시간
}
