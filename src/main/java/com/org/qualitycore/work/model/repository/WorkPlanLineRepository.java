package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.PlanLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface WorkPlanLineRepository extends JpaRepository<PlanLine, String> {}
