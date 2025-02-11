package com.org.qualitycore.masterdata.model.entity;

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
public class Workplace {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workplace_seq")
    @SequenceGenerator(name="workplace_swq", sequenceName ="WORKPLACE_ID", allocationSize =1)
    @Column(name ="WORKPLACE_ID")
    private int workplaceId; // 작업장 고유 ID

    @Column(name = "WORKPLACE_NAME ")
    private String workplaceName; // 작업장 이름

    @Column(name = "WORKPLACE_TYPE ")
    private String workplaceType; // 작업장 타입

    @Column(name = "WORKPLACE_CODE" )
    private String workplaceCode; // 작업장 코드

    @Column(name = "WORKPLACE_STATUS")
    private String workplaceStatus; // 작업장 코드

    @Column(name = "WORKPLACE_LOCATION")
    private String workplaceLocation; // 작업장 상태

    @Column(name="MANAGER_NAME ")
    private String managerName; // 작업장 위치

    @Column(name ="WORKPLACE_CAPACITY")
    private int workplaceCapacity; // 작업량 용량 / 생산 가능량

    @CreationTimestamp // insert 시 자동으로 sysdate 값 저장
    @Column(name ="CREATED_AT" , nullable = false ,updatable = false)
    private LocalDateTime createdAt; // 생성날짜

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt; // 수정날짜

}
