package com.org.qualitycore.attendance.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "근무자")
    private String worker;

    @Schema(description = "출근")
    private LocalDateTime checkIn; // 출근

    @Schema(description = "퇴근")
    private LocalDateTime checkOut; // 출근

    @Schema(description = "총 근무시간", example = "한달동안 일할 총 근무시간")
    private LocalDateTime totalTime;

    @Schema(description = "현재 근무시간", example = "한달동안 일한 현재 근무시간")
    private LocalDateTime workingTime;

    @Schema(description = "근태수정할 출근 시간")
    private LocalDateTime changeCheckIn; // 변경된 출근 시간

    @Schema(description = "근태수정할 퇴근 시간")
    private LocalDateTime changeCheckOut; // 변경된 퇴근 시간
}
