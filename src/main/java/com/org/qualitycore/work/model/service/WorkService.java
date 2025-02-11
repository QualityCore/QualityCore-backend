package com.org.qualitycore.work.model.service;

import com.org.qualitycore.work.model.dto.WorkDTO;
import com.org.qualitycore.work.model.entity.Work;
import com.org.qualitycore.work.model.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;
    private final ModelMapper modelMapper;

    // 작업지시서 메인화면 전체조회
    public List<WorkDTO> findAllWorkOrders() {

        List<Work> work = workRepository.findAll();

        return work.stream().map(works -> modelMapper.map(works, WorkDTO.class)).collect(Collectors.toList());
    }


    public WorkDTO findByWorkOrderCode(int workId) {

        Work work = workRepository.findById(workId).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(work, WorkDTO.class);
    }

    public void workOrderCreate(WorkDTO work) {

        workRepository.save(modelMapper.map(work, Work.class));
    }

    public void workOrderUpdate(WorkDTO work) {

        Work workUpdate = workRepository.findById(work.getWorkOrderId()).orElseThrow(IllegalArgumentException::new);

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

    public void workOrderDelete(int workId) {

        workRepository.deleteById(workId);

        modelMapper.map(workId, Work.class);
    }
}
