package com.org.qualitycore.work.model.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.org.qualitycore.work.model.entity.LineMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LineMaterialRepository extends JpaRepository<LineMaterial, String> {

    List<LineMaterial> findByWorkOrdersLotNo(String lotNo);

}
