package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.dto.WorkLotDTO;
import com.org.qualitycore.work.model.entity.*;
import com.org.qualitycore.work.model.repository.WorkRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Schema(description = "작업지시서 관련 Service")
public class WorkService {

        private final WorkRepository workRepository;
        private final ModelMapper mapper;
        private final JPAQueryFactory queryFactory;



    public List<WorkFindAllDTO> findAllWorkOrders() {

        QWorkOrders workOrders = QWorkOrders.workOrders;
        QEmployee employee = QEmployee.employee;
        QPlanProduct planProduct = QPlanProduct.planProduct;
        QPlanLine planLine = QPlanLine.planLine;
        QProgressStatus progressStatus = QProgressStatus.progressStatus;

        return queryFactory
                .select(Projections.fields(WorkFindAllDTO.class,
                        workOrders.lotNo.as("lotNo"),
                        workOrders.workProgress.as("workProgress"),
                        workOrders.workEtc.as("workEtc"),
                        employee.workTeam.as("workTeam"),
                        planLine.lineNo.as("lineNo"),
                        planLine.planQty.as("planQty"),
                        planLine.startDate.as("startDate"),
                        planLine.endDate.as("endDate"),
                        planProduct.productName.as("productName"),
                        planProduct.sizeSpec.as("sizeSpec"),
                        progressStatus.processStatus.as("processStatus")
                ))
                .from(workOrders)
                .leftJoin(workOrders.employee, employee)
                .leftJoin(workOrders.planProduct, planProduct)
                .leftJoin(workOrders.planLine, planLine)
                .leftJoin(workOrders.progressStatus, progressStatus)
                .fetch();
    }


    // 작업지시서 상세조회
    public WorkLotDTO findByWorkOrderCode(String lotNo) {

        QWorkOrders workOrders = QWorkOrders.workOrders;
        QEmployee employee = QEmployee.employee;
        QPlanProduct planProduct = QPlanProduct.planProduct;
        QPlanLine planLine = QPlanLine.planLine;
        QProgressStatus progressStatus = QProgressStatus.progressStatus;

        return queryFactory
                .select(Projections.fields(WorkLotDTO.class,
                        workOrders.lotNo.as("lotNo"),
                        workOrders.workProgress.as("workProgress"),
                        workOrders.workEtc.as("workEtc"),
                        employee.workTeam.as("workTeam"),
                        planLine.lineNo.as("lineNo"),
                        planLine.planQty.as("planQty"),
                        planLine.startDate.as("startDate"),
                        planLine.endDate.as("endDate"),
                        planProduct.productName.as("productName"),
                        planProduct.sizeSpec.as("sizeSpec"),
                        progressStatus.processStatus.as("processStatus")
                ))
                .from(workOrders)
                .leftJoin(workOrders.employee, employee)
                .leftJoin(workOrders.planProduct, planProduct)
                .leftJoin(workOrders.planLine, planLine)
                .leftJoin(workOrders.progressStatus, progressStatus)
                .where(workOrders.lotNo.eq(lotNo))
                .fetchOne();
    }




}
//
//    // 작업지시서 생성
//    @Transactional
//    public void workOrderCreate(WorkDTO work) {
//
//        workRepository.save(modelMapper.map(work, WorkOrder.class));
//    }
//
//
//    // 작업지시서 삭제
//    @Transactional
//    public void workOrderDelete(int workId) {
//        // 작업지시서가 존재하는지 확인
//        if (!workRepository.existsById(workId)) {
//
//            throw new ResourceNotFoundException("작업지시서를 찾을 수 없습니다.");
//
//        }
//
//        // 작업지시서가 존재하면 삭제
//        workRepository.deleteById(workId);
//
//        modelMapper.map(workId, WorkOrder.class);
//    }


