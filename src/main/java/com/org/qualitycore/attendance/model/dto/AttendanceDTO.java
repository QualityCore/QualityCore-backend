package com.org.qualitycore.attendance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "근태 관련 DTO")
public class AttendanceDTO {

    @Schema(description = "근태 코드(PK)")
    private String scheduleId;

    @Schema(description = "출근")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkIn;

    @Schema(description = "퇴근")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkOut;

    @Schema(description = "스케줄표 특이사항")
    private String scheduleEtc;

    @Schema(description = "근무상태", example = "출근, 휴가")
    private String workStatus;

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

    @Schema(description = "직원 ID")
    private String empId;
}
