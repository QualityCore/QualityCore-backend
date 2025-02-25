package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.LineMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineMaterialRepository extends JpaRepository<LineMaterial, String> {

    List<LineMaterial> findByWorkOrdersLotNo(String lotNo);
}
