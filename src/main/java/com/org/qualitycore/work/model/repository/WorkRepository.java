package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.Work;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Schema(description = "작업지시서 관련 Repository")
public interface WorkRepository extends JpaRepository<Work, Integer> {}
