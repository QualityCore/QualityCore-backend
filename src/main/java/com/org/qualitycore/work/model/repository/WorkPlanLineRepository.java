package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.PlanLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkPlanLineRepository extends JpaRepository<PlanLine, String> {
}
