package com.org.qualitycore.productionPlan.model.repository;

import com.org.qualitycore.productionPlan.model.entity.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<ProductionPlan,Long> {
}
