package com.org.qualitycore.attendance.model.service;

import com.org.qualitycore.attendance.model.dto.AttendanceDTO;
import com.org.qualitycore.attendance.model.entity.QAttendance;
import com.org.qualitycore.attendance.model.entity.QEmployee;
import com.org.qualitycore.attendance.model.repository.ScheduleRepository;
import com.org.qualitycore.work.model.repository.WorkRepository;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ModelMapper mapper;
    private final ScheduleRepository scheduleRepository;
    private final JPAQueryFactory queryFactory;

    public List<AttendanceDTO> findAllSchedule() {

        com.org.qualitycore.attendance.model.entity.QEmployee employee = QEmployee.employee;
        QAttendance schedule = QAttendance.attendance;

        return queryFactory
                .select(Projections.fields(AttendanceDTO.class,
                        employee.empName.as("empName"),
                        employee.email.as("email"),
                        employee.phone.as("phone"),
                        employee.profileImage.as("profileImage"),
                        employee.workTeam.as("workTeam"),
                        schedule.checkIn.as("checkIn"),  // LocalDateTime으로 반환
                        schedule.checkOut.as("checkOut"),  // LocalDateTime으로 반환
                        schedule.totalTime.as("totalTime"),
                        schedule.workingTime.as("workingTime")
                ))
                .from(employee)
                .join(schedule).on(employee.empId.eq(schedule.employee.empId)) // empId로 join을 명확히 설정
                .fetch();
    }
}
