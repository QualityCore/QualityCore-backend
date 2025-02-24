package com.org.qualitycore.standardinformation.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "WORK_ORDER")
@Schema(description = "작업정보 엔티티")
public class WorkOrder {

    @Id
    @Column(name ="LOT_NO" , nullable = false , updatable = false)
    @Schema(description = "작업지시 ID" , example = "LOT2025021301")
    private  String lotNo;


    @Column(name = "WORK_ETC" ,nullable = false ,updatable = false)
    @Schema(description = "특이사항" , example = "이상 무!")
    private String workEtc;
}
