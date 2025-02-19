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
                        schedule.workStatus.as("workStatus"),
                        schedule.scheduleEtc.as("scheduleEtc")
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
                        schedule.workStatus.as("workStatus"),
                        schedule.scheduleEtc.as("scheduleEtc")
                ))
                .from(employee)
                .join(schedule).on(employee.empId.eq(schedule.employee.empId))  // 직원과 근태 정보 조인
                .where(schedule.scheduleId.eq(scheduleId))  // scheduleId로 필터링
                .fetchOne();
    }

    @Transactional
    public void createSchedule(EmpScheduleCreateDTO schedule) {

        Employee employee = employeeRepository.findByEmpId(schedule.getEmpId());

        String maxScheduleId = scheduleRepository.findMaxScheduleId();

        String newScheduleId = generateNewScheduleId(maxScheduleId);

        Attendance attendance = modelMapper.map(schedule, Attendance.class);

        attendance.setScheduleId(newScheduleId);

        attendance.setEmployee(employee);

        scheduleRepository.save(attendance);
    }

    // auto increment 방식
    private String generateNewScheduleId(String maxScheduleId) {
        if (maxScheduleId == null) {
            return "SD001";  // 첫 번째 ID가 SD001
        }

        // "SD" 접두사를 제외한 숫자 부분 추출
        String numericPart = maxScheduleId.substring(2);  // 예: "SD123" -> "123"
        int newId = Integer.parseInt(numericPart) + 1;  // 숫자 부분에 +1

        // 3자리 숫자로 포맷
        return String.format("SD%03d", newId);
    }
}

