package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.processTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessTrackingRepository extends JpaRepository<processTracking, String> {

}
