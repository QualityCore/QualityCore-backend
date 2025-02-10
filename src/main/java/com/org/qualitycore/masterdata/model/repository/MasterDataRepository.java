package com.org.qualitycore.masterdata.model.repository;

import com.org.qualitycore.masterdata.model.entity.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterDataRepository extends JpaRepository<Workplace , Integer > {

}
