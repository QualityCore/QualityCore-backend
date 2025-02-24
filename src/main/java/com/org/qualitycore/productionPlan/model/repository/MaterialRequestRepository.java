package com.org.qualitycore.productionPlan.model.repository;

import com.org.qualitycore.productionPlan.model.entity.MaterialRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, String> {
    @Query("SELECT MAX(m.requestId) FROM MaterialRequest m")
    String findMaxRequestId();
}
