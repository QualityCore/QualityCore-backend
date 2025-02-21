package com.org.qualitycore.attendance.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpScheduleCreateDTO {

    @Schema(description = "근태 코드(PK)")
    private String scheduleId;

    @Schema(description = "출근")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkIn;

    @Schema(description = "퇴근")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkOut;

    @Schema(description = "근무상태", example = "출근, 휴가")
    private String workStatus;

    @Schema(description = "스케줄표 특이사항")
    private String scheduleEtc;

    @Schema(description = "직원 ID")
    private String empId;
}
