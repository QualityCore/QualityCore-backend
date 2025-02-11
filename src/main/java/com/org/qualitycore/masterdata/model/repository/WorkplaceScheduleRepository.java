package com.org.qualitycore.masterdata.model.repository;

import com.org.qualitycore.masterdata.model.entity.WorkplaceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WorkplaceScheduleRepository extends JpaRepository<WorkplaceSchedule, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM WorkplaceSchedule ws WHERE ws.workplace.workplaceCode = :workplaceCode ")

    void deleteByWorkplaceCode(@Param("workplaceCode") String workplaceCode);
}
