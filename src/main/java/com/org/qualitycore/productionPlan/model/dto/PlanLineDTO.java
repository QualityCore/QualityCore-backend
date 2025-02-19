package com.org.qualitycore.productionPlan.model.dto;

import com.org.qualitycore.productionPlan.model.entity.PlanLine;
import com.org.qualitycore.productionPlan.model.entity.PlanProduct;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlanLineDTO {
    private String planLineId;
    private String planProductId;
    private Integer lineNo;
    private Integer planBatchNo;
    private Integer planQty;
    private LocalDate startDate;
    private LocalDate endDate;

    public static PlanLineDTO fromEntity(PlanLine planLine) {
        return new PlanLineDTO(
                planLine.getPlanLineId(),
                planLine.getPlanProduct().getPlanProductId(),
                planLine.getLineNo(),
                planLine.getPlanBatchNo(),
                planLine.getPlanQty(),
                planLine.getStartDate(),
                planLine.getEndDate()
        );
    }

    public PlanLine toEntity() {
        PlanProduct planProduct = new PlanProduct();
        planProduct.setPlanProductId(this.planProductId);

        PlanLine planLine = new PlanLine();
        planLine.setPlanProduct(planProduct);
        planLine.setLineNo(this.lineNo);
        planLine.setPlanBatchNo(this.planBatchNo);
        planLine.setPlanQty(this.planQty);
        planLine.setStartDate(this.startDate);
        planLine.setEndDate(this.endDate);

        return planLine;
    }
}
