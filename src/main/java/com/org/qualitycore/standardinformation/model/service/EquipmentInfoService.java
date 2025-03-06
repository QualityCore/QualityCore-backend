package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.common.CloudinaryService;
import com.org.qualitycore.standardinformation.model.dto.EquipmentInfoDTO;
import com.org.qualitycore.standardinformation.model.entity.EquipmentInfo;
import com.org.qualitycore.standardinformation.model.entity.QEquipmentInfo;
import com.org.qualitycore.standardinformation.model.entity.QWorkplace;
import com.org.qualitycore.standardinformation.model.entity.Workplace;
import com.org.qualitycore.standardinformation.model.repository.EquipmentInfoRepository;
import com.org.qualitycore.standardinformation.model.repository.WorkplaceRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentInfoService {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;
    private final EquipmentInfoRepository equipmentInfoRepository;
    private final WorkplaceRepository workplaceRepository;
    private final CloudinaryService cloudinaryService;

    // 전체조회
    public List<EquipmentInfoDTO> findEquipmentAll() {
        QEquipmentInfo eq = QEquipmentInfo.equipmentInfo; // EQUIPMENT_INFO 테이블의 QueryDSL 객체
        QWorkplace wp = QWorkplace.workplace; // WORKPLACE 테이블의 QueryDSL 객체

        return queryFactory
                .select(Projections.fields(EquipmentInfoDTO.class,
                        eq.equipmentId.as("equipmentId"),
                        eq.equipmentName.as("equipmentName"),
                        eq.modelName.as("modelName"),
                        eq.manufacturer.as("manufacturer"),
                        eq.installDate.as("installDate"),
                        eq.equipmentStatus.as("equipmentStatus"),
                        eq.equipmentImage.as("equipmentImage"),
                        eq.equipmentEtc.as("equipmentEtc"),
                        wp.workplaceId.as("workplaceId"),
                        wp.workplaceName.as("workplaceName"),
                        wp.workplaceType.as("workplaceType")
                ))
                .from(eq)  // EquipmentInfo 엔티티에서 시작
                .join(eq.workplace, wp)  // fetch join
                .fetch();  // 여러 개의 장비 정보를 반환
    }

    // 상세조회
    public EquipmentInfoDTO findByCodeEquipment(String equipmentId) {
        QEquipmentInfo eq = QEquipmentInfo.equipmentInfo; // EQUIPMENT_INFO 테이블의 QueryDSL 객체
        QWorkplace wp = QWorkplace.workplace; // WORKPLACE 테이블의 QueryDSL 객체

        return queryFactory
                .select(Projections.fields(EquipmentInfoDTO.class,
                        eq.equipmentId.as("equipmentId"),
                        eq.equipmentName.as("equipmentName"),
                        eq.modelName.as("modelName"),
                        eq.manufacturer.as("manufacturer"),
                        eq.installDate.as("installDate"),
                        eq.equipmentStatus.as("equipmentStatus"),
                        eq.equipmentImage.as("equipmentImage"),
                        eq.equipmentEtc.as("equipmentEtc"),
                        wp.workplaceId.as("workplaceId"),
                        wp.workplaceName.as("workplaceName"),
                        wp.workplaceType.as("workplaceType")
                ))
                .from(eq)  // EquipmentInfo 엔티티에서 시작
                .join(eq.workplace, wp)  // fetch join
                .where(eq.equipmentId.eq(equipmentId))  // 특정 equipmentId에 해당하는 정보만 조회
                .fetchOne();  // 단일 결과 반환
    }

    // 설비등록
    @Transactional
    public void createEquipment(EquipmentInfoDTO equipmentDTO) {
        // 최대 equipmentId 조회
        String maxEquipmentId = equipmentInfoRepository.findMaxEquipmentId();
        String newEquipmentId = generateNewEquipmentId(maxEquipmentId);

        // EquipmentInfo Entity 생성 및 데이터 설정
        EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setEquipmentId(newEquipmentId);
        equipmentInfo.setEquipmentName(equipmentDTO.getEquipmentName());
        equipmentInfo.setModelName(equipmentDTO.getModelName());
        equipmentInfo.setManufacturer(equipmentDTO.getManufacturer());
        equipmentInfo.setInstallDate(equipmentDTO.getInstallDate());
        equipmentInfo.setEquipmentStatus(equipmentDTO.getEquipmentStatus());
        equipmentInfo.setEquipmentEtc(equipmentDTO.getEquipmentEtc());

        // Workplace 찾기
        Workplace workplace = workplaceRepository.findById(equipmentDTO.getWorkplaceId())
                .orElseThrow(() -> new IllegalArgumentException("Workplace not found"));
        equipmentInfo.setWorkplace(workplace);

        // 이미 업로드된 이미지 URL을 바로 사용
        if (equipmentDTO.getEquipmentImage() != null && !equipmentDTO.getEquipmentImage().isEmpty()) {
            equipmentInfo.setEquipmentImage(equipmentDTO.getEquipmentImage());  // 업로드된 이미지 URL을 EquipmentInfo에 설정
        }

        // 데이터 저장
        equipmentInfoRepository.save(equipmentInfo);
    }

    // auto increment 방식
    private String generateNewEquipmentId(String maxEquipmentId) {
        if (maxEquipmentId == null) {
            return "EQ001";  // 첫 번째 ID가 EQ001
        }

        // "EQ" 접두사를 제외한 숫자 부분 추출
        String numericPart = maxEquipmentId.substring(2);  // 예: "EQ123" -> "123"
        int newId = Integer.parseInt(numericPart) + 1;  // 숫자 부분에 +1

        // 3자리 숫자로 포맷
        return String.format("EQ%03d", newId);
    }

    // 설비수정
    @Transactional
    public void updateEquipment(EquipmentInfoDTO equipment) {

        EquipmentInfo equipmentInfoDTO = equipmentInfoRepository.findById(equipment.getEquipmentId()).orElseThrow(IllegalArgumentException::new);

        EquipmentInfo equipmentInfo = equipmentInfoDTO.
                                      toBuilder().
                                      equipmentStatus(equipment.getEquipmentStatus()).
                                      equipmentEtc(equipment.getEquipmentEtc()).
                                      build();

        equipmentInfoRepository.save(equipmentInfo);
    }

    // 설비삭제
    @Transactional
    public void deleteEquipment(String equipmentId) {

        equipmentInfoRepository.deleteById(equipmentId);

        modelMapper.map(equipmentId, EquipmentInfoDTO.class);
    }
}
