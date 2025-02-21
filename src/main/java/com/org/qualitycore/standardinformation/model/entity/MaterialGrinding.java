package com.org.qualitycore.standardinformation.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="MATERIAL_GRINDING")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "분쇄 공정 엔티티")
public class MaterialGrinding {

    @Id
    @Column(name = "GRINDING_ID" , nullable = false , updatable = false )
    @Schema(description = "분쇄공정 ID" , example = "GR001")
    private String grindingId;


    @Column(name = "LOT_NO" , n)



    @ManyToOne
    @JoinColumn(name = "LOT_NO" , nullable = false)
    @Schema(description = "작업지시 ID" , example ="LOT2025021301")
    private  WorkOrder workOrder;

    @Column(name ="MAIN_MATERIAL" , nullable = false )
    @Schema(description = "주원료" , example ="쌀")
    private String mainMaterial ;

    @Column(name = "MAIN_MATERIAL_INPUT_VOLUME" , nullable = false)
    @Schema(description = "주원료 투입량" , example = "50.00")
    private double mainMaterialInputVolume;


    @Column(name ="MALT_TYPE" , nullable = false )
    @Schema(description = "맥아 종류" , example = "필스너 몰트" )
    private String maltType;

    @Column(name = "MALT_INPUT_VOLUME", nullable = false)
    @Schema(description ="맥아 투입량" , example = "450")
    private Double maltInputVolume;

    @Column(name = "GRIND_INTERVAL_SETTING", nullable = false)
    @Schema(description = "분쇄 간격 설정" , example = "1.00" )
    private Double grindIntervalSetting;

    @Column(name = "GRIND_SPEED_SETTING", nullable = false)
    @Schema(description = "분쇄 속도 설정" , example = "150.00" )
    private Double grindSpeedSetting;

    @Column(name = "GRIND_DURATION", nullable = false)
    @Schema(description = "소요시간 " , example = "40" )
    private Integer grindDuration;

    @Column(name = "NOTES")
    @Schema(description = "메모사항" , example = "작업자 : 강동원  작업완료" )
    private String notes;

    @Column(name = "START_TIME" , nullable = false)
    @Schema(description = "시작시간" , example = "2025-02-12T10:15:30")
    private LocalDateTime startTime;

    @Column(name = "EXPECTED_END_TIME" , nullable = false)
    @Schema(description = "예상 종료 시간" , example = "2025-02-12T10:55:30")
    private LocalDateTime expectedEndTime;

    @Column(name = "ACTUAL_END_TIME" , nullable = false)
    @Schema(description = "실제 종료 시간" , example = "2025-02-12T11:00:30")
    private LocalDateTime actualEndTime;


}



