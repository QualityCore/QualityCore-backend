package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.LineMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface LineMaterialRepository extends JpaRepository<LineMaterial, String> {

    List<LineMaterial> findByWorkOrdersLotNo(String lotNo);

    // 남규 !  레파지토리 잘 빌려쓰겠습니다.
    @Query("SELECT lm FROM LineMaterial lm JOIN FETCH lm.workOrders wo WHERE wo.lotNo = :lotNo")
    List<LineMaterial> findByLotNo(@Param("lotNo") String lotNo);

    // 남규 레파지토리 잘쓸께요
    @Query("SELECT lm FROM LineMaterial lm ORDER BY lm.lotNo ASC") // 최신순 정렬
    List<LineMaterial> findAllLineMaterial();



}
