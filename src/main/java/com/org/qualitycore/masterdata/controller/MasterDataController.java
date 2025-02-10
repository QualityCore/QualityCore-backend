package com.org.qualitycore.masterdata.controller;

import com.org.qualitycore.masterdata.model.entity.Workplace;
import com.org.qualitycore.masterdata.model.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/masterdata")
@RequiredArgsConstructor
public class MasterDataController {

    private  final MasterDataService masterDataService;



    // 작업장 전체 조회
    @GetMapping("/workplaces")
    public List<Workplace> getAllWorkplaces(){
        return masterDataService.getAllWorkplaces();
    }










}
