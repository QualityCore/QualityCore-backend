package com.org.qualitycore.attendance.model.repository;

import com.org.qualitycore.attendance.model.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Attendance, String> {

    @Query("SELECT MAX(a.scheduleId) FROM Attendance a")
    String findMaxScheduleId();
}
