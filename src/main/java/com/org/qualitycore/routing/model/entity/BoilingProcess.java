package com.org.qualitycore.routing.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "BOILING_PROCESS")
@Entity(name = "BoilingProcessEntity")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoilingProcess {

    // 끓임공정

    @Id
    @Column(name = "BOILING_ID")
    private String boilingId;

    @Column(name = "LOT_NO")
    private String lotNo;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "EXPECTED_END_TIME")
    private LocalDateTime expectedEndTime;
}
