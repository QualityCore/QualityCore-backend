package com.org.qualitycore.work.model.service;

import com.org.qualitycore.exception.ResourceNotFoundException;
import com.org.qualitycore.work.model.dto.WorkDTO;
import com.org.qualitycore.work.model.entity.Work;
import com.org.qualitycore.work.model.repository.WorkRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Schema(description = "작업지시서 관련 Service")
public class WorkService {

    private final WorkRepository workRepository;
    private final ModelMapper modelMapper;

    // 작업지시서 메인화면 전체조회
    public List<WorkDTO> findAllWorkOrders() {

        List<Work> work = workRepository.findAll();

        return work.stream().
                map(works -> modelMapper.map(works, WorkDTO.class)).
                collect(Collectors.toList());
    }

    // 작업지시서 상세조회
    public WorkDTO findByWorkOrderCode(int workId) {

        Work work = workRepository.findById(workId).
                orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(work, WorkDTO.class);
    }

    // 작업지시서 생성
    @Transactional
    public void workOrderCreate(WorkDTO work) {

        workRepository.save(modelMapper.map(work, Work.class));
    }

    // 작업지시서 수정
    @Transactional
    public void workOrderUpdate(WorkDTO work) {

        Work workUpdate = workRepository.findById(work.getWorkOrderId()).
                orElseThrow(() -> new IllegalArgumentException("작업지시서를 찾을 수 없습니다."));

        Work workUpdates = workUpdate.toBuilder().
                           workTeam(work.getWorkTeam()).
                           workQuantity(work.getWorkQuantity()).
                           workStartDate(work.getWorkStartDate()).
                           workEndDate(work.getWorkEndDate()).
                           workOrderStatus(work.getWorkOrderStatus()).
                           workEtc(work.getWorkEtc()).
                           build();

        // 수정할거 작업조, 작업수량, 작업시작일, 작업종료일, 작업상태, 특이사항

        workRepository.save(workUpdates);
    }

    // 작업지시서 삭제
    @Transactional
    public void workOrderDelete(int workId) {
        // 작업지시서가 존재하는지 확인
        if (!workRepository.existsById(workId)) {

            throw new ResourceNotFoundException("작업지시서를 찾을 수 없습니다.");

        }

        // 작업지시서가 존재하면 삭제
        workRepository.deleteById(workId);

        modelMapper.map(workId, Work.class);
    }

}
