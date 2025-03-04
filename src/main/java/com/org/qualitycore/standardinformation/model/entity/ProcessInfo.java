package com.org.qualitycore.standardinformation.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "PROCESS_STAGE")
public class ProcessInfo {

    @Id
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PROCESS_ID")
    private String processId;

    @Column(name = "PROCESS_NAME")
    private String processName;

    @Column(name = "PROCESS_CODE")
    private String processCode;

    @Column(name = "PROCESS_DESCRIPTION")
    private String processDescription;

    @Column(name = "PROCESS_TYPE")
    private String processType;

    @Column(name = "BEER_TYPE")
    private String beerType;

    @Column(name = "PROCESS_DURATION")
    private String processDuration;

    @Column(name = "PROCESS_STATUS")
    private String processStatus;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;
}
