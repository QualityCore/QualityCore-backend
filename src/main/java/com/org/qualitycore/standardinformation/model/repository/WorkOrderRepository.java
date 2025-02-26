package com.org.qualitycore.standardinformation.model.repository;

import com.org.qualitycore.standardinformation.model.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, String > {

    @Query("SELECT w FROM WorkOrder w WHERE TRIM(UPPER(w.lotNo)) = UPPER(:lotNo)")
    Optional<WorkOrder> findByLotNo (@Param("lotNo") String lotNo);

}
