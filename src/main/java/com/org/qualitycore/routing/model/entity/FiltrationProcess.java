package com.org.qualitycore.routing.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Entity(name = "FiltrationProcessEntity")
@Table(name = "FILTRATION_PROCESS")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FiltrationProcess {

    // 여과작업

    @Id
    @Column(name = "FILTRATION_ID")
    private String filtrationId;

    @Column(name = "LOT_NO")
    private String lotNo;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "EXPECTED_END_TIME")
    private LocalDateTime expectedEndTime;

}
