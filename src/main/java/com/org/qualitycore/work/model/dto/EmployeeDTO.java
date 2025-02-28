package com.org.qualitycore.work.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDTO {

    private String empId; // 사원코드
    private String passWord; // 비밀번호
    private String empName; // 사원이름
    private String email; // 이메일
    private String phone; // 휴대폰
    private String authority; // 사용자 권한
    private String profileImage; // 프로필사진
    private String workTeam; // 작업조

}
