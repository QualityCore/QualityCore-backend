package com.org.qualitycore.attendance.model.repository;

import com.org.qualitycore.attendance.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByEmpId(String empId);
}
