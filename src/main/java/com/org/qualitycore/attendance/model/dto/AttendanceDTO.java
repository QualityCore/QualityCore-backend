package com.org.qualitycore.attendance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "근태 관련 DTO")
public class AttendanceDTO {

    @Schema(description = "근태 코드(PK)")
    private int attendanceId;

    @Schema(description = "출근")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkIn; // 출근

    @Schema(description = "퇴근")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOut; // 출근

    @Schema(description = "총 근무시간", example = "한달동안 일할 총 근무시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime totalTime;

    @Schema(description = "현재 근무시간", example = "한달동안 일한 현재 근무시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime workingTime;

    @Schema(description = "직원이름")
    private String empName;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "휴대폰번호")
    private String phone;

    @Schema(description = "프로필사진")
    private String profileImage;

    @Schema(description = "작업조")
    private String workTeam;
}
