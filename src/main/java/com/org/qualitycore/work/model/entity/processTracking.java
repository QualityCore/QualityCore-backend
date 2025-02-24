package com.org.qualitycore.work.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "PROCESS_TRACKING")
public class processTracking {

    @Id
    @Column(name = "TRACKING_ID")
    private String trackingId;

    @Column(name = "LOT_NO")
    private String lotNo;

    @Column(name = "STATUS_CODE")
    private String statusCode;

    @Column(name = "PROCESS_STATUS")
    private String processStatus;

    @Column(name = "PROCESS_NAME")
    private String processName;
}
