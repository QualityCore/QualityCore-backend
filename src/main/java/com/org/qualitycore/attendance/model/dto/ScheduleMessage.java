package com.org.qualitycore.attendance.model.dto;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScheduleMessage {

    private int code;

    private String message;

    private Map<String, Object> result;
}
