package com.org.qualitycore.standardinformation.model.repository;

import com.org.qualitycore.standardinformation.model.entity.FermentationDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FermentationDetailsRepository extends JpaRepository<FermentationDetails,String> {
    @Query("SELECT MAX(CAST(SUBSTRING(f.fermentationId, 3) AS int)) FROM FermentationDetails f")
    Integer findMaxFermentationId();
}
