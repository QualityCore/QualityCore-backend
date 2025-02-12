package com.org.qualitycore.masterdata.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkplaceDTO {

    private Long workplaceId; // 작업장 고유 ID
    private String workplaceName; // 작업장 이름
    private String workplaceType; // 작업장 타입
    private String workplaceCode; // 작업장 코드
    private String workplaceStatus; // 작업장 상태
    private String workplaceLocation; // 작업장 위치
    private String managerName; // 작업 책임자
    private int workplaceCapacity; // 작업량 용량 / 생산 가능량
    private LocalDateTime createdAt; // 생성날짜 (응답전용)
    private LocalDateTime updatedAt; // 수정날짜 (응답전용)


}
