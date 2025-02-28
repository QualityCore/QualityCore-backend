package com.org.qualitycore.standardinformation.model.repository;

import com.org.qualitycore.standardinformation.model.entity.MaterialGrinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialGrindingRepository extends JpaRepository<MaterialGrinding, String > {

    @Query("SELECT MAX(CAST(SUBSTRING(g.grindingId, 3) AS int)) FROM MaterialGrinding g")
    Integer findMaxGrindingId();

}
