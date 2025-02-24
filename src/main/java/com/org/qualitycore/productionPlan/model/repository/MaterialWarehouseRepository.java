package com.org.qualitycore.productionPlan.model.repository;

import com.org.qualitycore.productionPlan.model.entity.MaterialWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialWarehouseRepository extends JpaRepository<MaterialWarehouse, String> {

}
