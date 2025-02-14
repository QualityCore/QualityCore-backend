package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.WorkFindAllDTO;
import com.org.qualitycore.work.model.repository.WorkRepository;
import com.org.qualitycore.work.model.entity.Employee;
import com.org.qualitycore.work.model.repository.WorkRepository;
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


    // 작업지시서 메인화면 전체조회
    public List<WorkFindAllDTO> findAllWorkOrders() {


        return workRepository.findAllWorkOrders();
    }

//
//    // 작업지시서 상세조회
//    public WorkDTO findByWorkOrderCode(int workId) {
//
//        WorkOrder work = workRepository.findById(workId).
//                orElseThrow(IllegalArgumentException::new);
//
//        return modelMapper.map(work, WorkDTO.class);
//    }
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

}
