package com.org.qualitycore.attendance.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "EMP_SCHEDULE")
@Entity
@Builder(toBuilder = true)
public class Attendance {

    @Id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId; // 근태코드

    @Column(name = "CHECK_IN")
    private Date checkIn; // 근무시간

    @Column(name = "CHECK_OUT")
    private Date checkOut; // 근무종료시간

    @Column(name = "SCHEDULE_ETC")
    private String scheduleEtc;

    @Column(name = "WORK_STATUS")
    private String workStatus; // 근무상태

    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    private Employee employee;


}
