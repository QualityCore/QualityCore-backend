package com.org.qualitycore.standardinformation.model.entity;

import com.org.qualitycore.work.model.entity.LineMaterial;
import com.org.qualitycore.work.model.entity.processTracking;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MATURATION_DETAILS")
@Schema(description = "숙성 상세 공정 엔티티")
public class MaturationDetails {

    @Id
    @Column(name = "MATURATION_ID", nullable = false, updatable = false)
    @Schema(description = "숙성 공정 ID", example = "MAR001")
    private String maturationId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maturationDetails")
    @Schema(description = "작업지시 ID", example = "LOT2025021301")
    private List<LineMaterial> lineMaterials;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "공정 상태 체크")
    private processTracking processTracking;

    @Column(name = "LOT_NO", nullable = false)
    @Schema(description = "작업지시 ID", example = "LOT2025021201")
    private String lotNo;

    @Column(name = "MATURATION_TIME", nullable = false)
    @Schema(description = "숙성 소요 시간 (분)", example = "1440")
    private Integer maturationTime;

    @Column(name = "START_TEMPERATURE", nullable = false)
    @Schema(description = "숙성 시작 온도 (°C)", example = "15.0")
    private Double startTemperature;

    @CreationTimestamp
    @Column(name = "START_TIME", nullable = false)
    @Schema(description = "작업 시작 시간", example = "2025-02-12T16:00:00")
    private LocalDateTime startTime;

    @Column(name = "EXPECTED_END_TIME", nullable = false)
    @Schema(description = "예상 종료 시간", example = "2025-02-19T16:00:00")
    private LocalDateTime expectedEndTime;

    @UpdateTimestamp
    @Column(name = "ACTUAL_END_TIME")
    @Schema(description = "실제 종료 시간", example = "2025-02-19T18:00:00")
    private LocalDateTime actualEndTime;

    @Column(name = "NOTES")
    @Schema(description = "메모 사항", example = "숙성 완료, 향미 안정적")
    private String notes;

    @PrePersist
    public void prePersist() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        if (expectedEndTime == null && maturationTime != null) {
            expectedEndTime = startTime.plusMinutes(maturationTime.longValue());
        }
    }

    @Override
    public String toString() {
        return "MaturationDetails{" +
                "maturationId='" + maturationId + '\'' +
                ", lotNo='" + lotNo + '\'' +
                ", maturationTime=" + maturationTime +
                ", startTemperature=" + startTemperature +
                ", startTime=" + startTime +
                ", expectedEndTime=" + expectedEndTime +
                ", actualEndTime=" + actualEndTime +
                ", notes='" + notes + '\'' +
                '}';
    }
}
