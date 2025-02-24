package com.org.qualitycore.productionPlan.model.dto;

import com.org.qualitycore.productionPlan.model.entity.MaterialRequest;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequestDTO {


    private String requestId;
    private String planMaterialId;
    private Double requestQty;
    private LocalDate deliveryDate;
    private String reason;
    private String note;

    public MaterialRequest toEntity() {
        MaterialRequest entity = new MaterialRequest();
        entity.setRequestId(this.requestId);
        entity.setRequestQty(this.requestQty);
        entity.setDeliveryDate(this.deliveryDate);
        entity.setReason(this.reason);
        entity.setNote(this.note);
        return entity;
    }
}