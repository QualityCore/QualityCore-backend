package com.org.qualitycore.work.model.entity;

import com.org.qualitycore.standardinformation.model.entity.MashingProcess;
import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "workOrders")
@Entity
@Table(name = "LINE_MATERIAL")
@Builder
public class LineMaterial {

    @Id
    @Column(name = "LINE_MATERIAL_ID")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String lineMaterialId;

    @Column(name = "MATERIAL_NAME")
    private String materialName;

    @Column(name = "TOTAL_QTY")
    private String totalQty;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REQUIRED_QTY_PER_UNIT")
    private double requiredQtyPerUnit;

    @Column(name = "PROCESS_STEP")
    private String processStep;

    //남규 @JoinColumn(referencedColumnName = "LOT_NO") 추가입력함.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOT_NO", referencedColumnName = "LOT_NO")
    private WorkOrders workOrders;

    // 남규  당화 공정 추가함 fk 추가
    @ManyToOne
    @JoinColumn(name = "MASHING_ID")  // 외래키 설정
    private MashingProcess mashingProcess;  // <-- 이 필드명을 사용해야 함

    // 남규 분쇄 공정 추가함 fk 추가
    @ManyToOne
    @JoinColumn(name = "GRINDING_ID")  // 외래키 설정
    private MaterialGrinding materialGrinding;  // <-- 이 필드명을 사용해야 함

}

