package com.org.qualitycore.standardinformation.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name ="WORKPLACE")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "작업장정보 엔티티")
public class Workplace {

    @Id
    @Column(name ="WORKPLACE_ID" , nullable = false , updatable = false)
    @Schema(description = "작업장 고유 ID", example = "WO001")
    private String workplaceId; // 작업장 고유 ID

    @Column(name ="LINE_ID", nullable = false)
    @Schema(description = "LINE_ID",example = "LINE001")
    private String lineId;

    @Column(name ="WORKPLACE_NAME",nullable = false)
    @Schema(description = "작업장 이름", example = "제1작업장")
    private String workplaceName; // 작업장 이름

    @Column(name ="WORKPLACE_TYPE",nullable = false)
    @Schema(description = "작업장 타입", example = "분쇄")
    private String workplaceType; // 작업장 타입

    @Column(name ="WORKPLACE_CODE" , nullable = false)
    @Schema(description = "작업장 코드 (유니크)", example = "W001")
    private String workplaceCode; // 작업장 코드


    @Column(name ="WORKPLACE_STATUS",nullable = false)
    @Schema(description = "작업장 상태", example = "가동 중")
    private String workplaceStatus; // 작업장 코드

    @Column(name ="WORKPLACE_LOCATION",nullable = false)
    @Schema(description = "작업장 위치", example = "서울 공장 1층")
    private String workplaceLocation; // 작업장 상태

    @Column(name="MANAGER_NAME",nullable = false)
    @Schema(description = "작업장 책임자", example = "김철수")
    private String managerName; // 작업장 위치

    @Column(name ="WORKPLACE_CAPACITY",nullable = false)
    @Schema(description = "작업량 용량 / 생산 가능량", example = "1000")
    private Integer workplaceCapacity; // 작업량 용량 / 생산 가능량

    @CreationTimestamp // insert 시 자동으로 sysdate 값 저장
    @Column(name ="CREATED_AT" , nullable = false)
    @Schema(description = "작업장 생성 날짜", example = "2024-02-12T10:15:30")
    private LocalDateTime createdAt; // 생성날짜

    @UpdateTimestamp
    @Column(name ="UPDATED_AT")
    @Schema(description = "작업장 수정 날짜", example = "2024-02-12T11:00:00")
    private LocalDateTime updatedAt;  // 수정 날짜
}


