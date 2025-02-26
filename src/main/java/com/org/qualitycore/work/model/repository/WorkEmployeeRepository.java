package com.org.qualitycore.work.model.repository;

import com.org.qualitycore.work.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkEmployeeRepository extends JpaRepository<Employee, String> { }
