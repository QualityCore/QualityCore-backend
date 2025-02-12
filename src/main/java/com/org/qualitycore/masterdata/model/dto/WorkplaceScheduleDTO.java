package com.org.qualitycore.masterdata.model.dto;

import com.org.qualitycore.masterdata.model.entity.Workplace;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkplaceScheduleDTO {
    private Long scheduleID; // 스케줄 고유 id
    private Workplace workplace; // 부모(작업장등록) 엔티티와 관계설정
    private String shiftType; // 작업조 선택
    private LocalDateTime startTime; //작업시작시간
    private LocalDateTime endTime; //작업종료시간
    private  LocalDateTime createdAt; // 생성날짜

}
