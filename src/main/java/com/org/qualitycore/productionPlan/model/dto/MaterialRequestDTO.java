package com.org.qualitycore.productionPlan.model.dto;

import com.org.qualitycore.productionPlan.model.entity.MaterialRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequestDTO {

    private List<MaterialRequestInfo> materials; // 부족 자재 목록
    private String requestId;
    private String planMaterialId;
    private Double requestQty;
    private LocalDate deliveryDate;
    private String reason;
    private String note;


    // 내부 클래스 추가
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MaterialRequestInfo {
        private String materialId;
        private String materialName;
        private Double requestQty;
        private String productId; // 필요하다면 추가
    }

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