package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.WorkOrders;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@Schema(description = "작업지시서 관련 Repository")
public interface WorkRepository extends JpaRepository<WorkOrders, String> {
    Optional<WorkOrders> findTopByOrderByLotNoDesc();


    Optional<WorkOrders> findByLotNo(String lotNo);
}
