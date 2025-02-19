package com.org.qualitycore.attendance.model.service;

import com.org.qualitycore.attendance.model.dto.AttendanceDTO;
import com.org.qualitycore.attendance.model.dto.EmpScheduleCreateDTO;
import com.org.qualitycore.attendance.model.entity.Attendance;
import com.org.qualitycore.attendance.model.entity.Employee;
import com.org.qualitycore.attendance.model.entity.QAttendance;
import com.org.qualitycore.attendance.model.entity.QEmployee;
import com.org.qualitycore.attendance.model.repository.EmployeeRepository;
import com.org.qualitycore.attendance.model.repository.ScheduleRepository;
import com.org.qualitycore.work.model.repository.WorkRepository;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;
    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    public List<AttendanceDTO> findAllSchedule() {

        com.org.qualitycore.attendance.model.entity.QEmployee employee = QEmployee.employee;
        QAttendance schedule = QAttendance.attendance;

        return queryFactory
                .select(Projections.fields(AttendanceDTO.class,
                        employee.empId.as("empId"),
                        employee.empName.as("empName"),
                        employee.email.as("email"),
                        employee.phone.as("phone"),
                        employee.profileImage.as("profileImage"),
                        employee.workTeam.as("workTeam"),
                        schedule.scheduleId.as("scheduleId"),
                        schedule.checkIn.as("checkIn"),
                        schedule.checkOut.as("checkOut"),
                        schedule.totalTime.as("totalTime"),
                        schedule.workingTime.as("workingTime"),
                        schedule.workStatus.as("workStatus")
                ))
                .from(employee)
                .join(schedule).on(employee.empId.eq(schedule.employee.empId))
                .fetch();
    }

    public AttendanceDTO findByCodeSchedule(String scheduleId) {
        com.org.qualitycore.attendance.model.entity.QEmployee employee = QEmployee.employee;
        QAttendance schedule = QAttendance.attendance;

        return queryFactory
                .select(Projections.fields(AttendanceDTO.class,
                        employee.empId.as("empId"),
                        employee.empName.as("empName"),
                        employee.email.as("email"),
                        employee.phone.as("phone"),
                        employee.profileImage.as("profileImage"),
                        employee.workTeam.as("workTeam"),
                        schedule.scheduleId.as("scheduleId"),
                        schedule.checkIn.as("checkIn"),
                        schedule.checkOut.as("checkOut"),
                        schedule.totalTime.as("totalTime"),
                        schedule.workingTime.as("workingTime"),
                        schedule.workStatus.as("workStatus")
                ))
                .from(employee)
                .join(schedule).on(employee.empId.eq(schedule.employee.empId))  // 직원과 근태 정보 조인
                .where(schedule.scheduleId.eq(scheduleId))  // scheduleId로 필터링
                .fetchOne();
    }

    @Transactional
    public void createSchedule(EmpScheduleCreateDTO schedule) {

        Employee employee = employeeRepository.findByEmpId(schedule.getEmpId());

        Attendance attendance = modelMapper.map(schedule, Attendance.class);

        attendance.setEmployee(employee);

        scheduleRepository.save(attendance);
    }
}

