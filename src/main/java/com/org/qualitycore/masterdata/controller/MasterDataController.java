package com.org.qualitycore.masterdata.controller;

import com.org.qualitycore.masterdata.model.dto.WorkplaceDTO;
import com.org.qualitycore.masterdata.model.entity.MasterDataMessage;
import com.org.qualitycore.masterdata.model.entity.Workplace;
import com.org.qualitycore.masterdata.model.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/masterdata")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;
    private final ModelMapper modelMapper;


    // 작업장 전체 조회
    @GetMapping("/workplaces")
    public List<WorkplaceDTO> getAllWorkplaces() {
        List<Workplace> workplaces = masterDataService.getAllWorkplaces();
        return workplaces.stream()
                .map(workplace -> modelMapper.map(workplace, WorkplaceDTO.class))
                .collect(Collectors.toList());
    }

    // 작업장 등록
    @PostMapping("/workplaces")
    public ResponseEntity<MasterDataMessage> createWorkplace(@RequestBody WorkplaceDTO workplaceDTO) {
        Workplace savedWorkplace = masterDataService.creactWorkplace(workplaceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MasterDataMessage(HttpStatus.CREATED.value(),
                        "등록에 성공했어!! 축하해! ID :" + savedWorkplace.getWorkplaceId()));
    }


    // 작업장 수정
    @PutMapping("/workplaces")
    public ResponseEntity<MasterDataMessage> updateWorkplace(@RequestBody WorkplaceDTO workplaceDTO){
        Workplace savedWorkplace = masterDataService.updateWorkplace(workplaceDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MasterDataMessage(HttpStatus.CREATED.value(),
                        "등록에 성공했어!! 축하해! ID :" + savedWorkplace.getWorkplaceId()));
    }
    }













