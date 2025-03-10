package com.org.qualitycore.standardinformation.model.repository;

import com.org.qualitycore.standardinformation.model.entity.FermentationTimedLog;
import com.org.qualitycore.standardinformation.model.entity.MaturationTimedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaturationTimedLogRepository extends JpaRepository<MaturationTimedLog,Long> {

    List<MaturationTimedLog> findAllByMaturationDetails_MaturationId(String maturationId);
}
