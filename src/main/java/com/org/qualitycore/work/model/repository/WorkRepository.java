package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.entity.WorkOrders;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Schema(description = "작업지시서 관련 Repository")
public interface WorkRepository extends JpaRepository<WorkOrders, String> {

    @Query("SELECT new com.org.qualitycore.work.model.dto.WorkFindAllDTO(" +
            "w.lotNo, " +
            "w.workProgress, " +
            "w.workEtc, " +
            "e.workTeam, " +
            "p.lineNo, " +
            "p.planQty, " +
            "TO_CHAR(p.startDate, 'YYYY-MM-DD') AS startDate, " +
            "TO_CHAR(p.endDate, 'YYYY-MM-DD') AS endDate, " +
            "r.productName, " +
            "r.sizeSpec, " +
            "s.processStatus) " +
            "FROM WorkOrders w " +
            "LEFT JOIN w.employee e " +
            "LEFT JOIN w.planProduct r " +
            "LEFT JOIN w.planLine p " +
            "LEFT JOIN w.progressStatus s")
    List<WorkFindAllDTO> findAllWorkOrders();

}
