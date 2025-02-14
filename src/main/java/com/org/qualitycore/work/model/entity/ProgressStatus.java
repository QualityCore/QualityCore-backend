package com.org.qualitycore.work.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "PROGRESS_STATUS")
public class ProgressStatus {

    @Id
    @Column(name = "STATUS_CODE")
    private String statusCode; // 작업지시 ID(FK)

    @Column(name = "ENTIRE_PROCESS")
    private int entireProcess; // 전체공정 수

    @Column(name = "PROCESS_COUNT")
    private int processCount; // 현재공정 카운트

    @Column(name = "PROCESS_STATUS")
    private String processStatus; // 현재공정
}
