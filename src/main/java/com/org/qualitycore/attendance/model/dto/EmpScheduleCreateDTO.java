package com.org.qualitycore.attendance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpScheduleCreateDTO {

    @Schema(description = "근태 코드(PK)")
    private String scheduleId;

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

    @Schema(description = "근무상태", example = "출근, 휴가")
    private String workStatus;

    @Schema(description = "직원 ID")
    private String empId;
}
