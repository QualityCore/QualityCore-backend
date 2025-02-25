package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.PlanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkPlanProductRepository extends JpaRepository<PlanProduct, String> {
}
