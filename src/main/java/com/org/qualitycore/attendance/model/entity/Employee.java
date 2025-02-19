package com.org.qualitycore.attendance.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "emp_schedule")
@Table(name = "EMPLOYEE")
public class Employee {

    @Id
    @Column(name = "EMP_ID")
    private String empId;

    @Column(name = "PASSWORD")
    private String passWord;

    @Column(name = "EMP_NAME")
    private String empName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "AUTHORITY")
    private String authority;

    @Column(name = "PROFILE_IMAGE")
    private String profileImage;

    @Column(name = "WORK_TEAM")
    private String workTeam;

}
