package com.org.qualitycore.standardinformation.model.service;

import com.org.qualitycore.standardinformation.model.dto.LabelInfoDTO;
import com.org.qualitycore.standardinformation.model.entity.QLabelInfo;
import com.org.qualitycore.standardinformation.model.entity.QProductBom2;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelInfoService {

    private final JPAQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    // 전체조회
    // 전체조회
    public List<LabelInfoDTO> findAllLabelInfo() {
        QLabelInfo labelInfo = QLabelInfo.labelInfo;
        QProductBom2 productBomEntity = QProductBom2.productBom2;  // QProductBom을 QProductBomEntity로 변경

        List<LabelInfoDTO> labelInfoList = queryFactory
                .select(Projections.fields(LabelInfoDTO.class,
                        labelInfo.labelId.as("labelId"),
                        labelInfo.brandName.as("brandName"),
                        labelInfo.productionDate.as("productionDate"),
                        labelInfo.labelImage.as("labelImage"),
                        labelInfo.beerSupplier.as("beerSupplier"),
                        productBomEntity.productId.as("productId"),  // productBom -> productBomEntity로 변경
                        productBomEntity.productName.as("productName"),  // productBom -> productBomEntity로 변경
                        productBomEntity.sizeSpec.as("sizeSpec"),  // productBom -> productBomEntity로 변경
                        productBomEntity.alcPercent.as("alcPercent")  // productBom -> productBomEntity로 변경
                ))
                .from(labelInfo)
                .join(labelInfo.productBom, productBomEntity)  // labelInfo.productBom -> productBomEntity로 변경
                .on(labelInfo.productBom.productId.eq(productBomEntity.productId)) // productBom.productId -> productBomEntity.productId로 변경
                .fetch();

        return labelInfoList;
    }

    // 상세조회
    public LabelInfoDTO findByIdLabelInfo(String labelId) {
        QLabelInfo labelInfo = QLabelInfo.labelInfo;
        QProductBom2 productBomEntity = QProductBom2.productBom2;  // QProductBom을 QProductBomEntity로 변경

        LabelInfoDTO labelInfoDTO = queryFactory
                .select(Projections.fields(LabelInfoDTO.class,
                        labelInfo.labelId.as("labelId"),
                        labelInfo.brandName.as("brandName"),
                        labelInfo.productionDate.as("productionDate"),
                        labelInfo.labelImage.as("labelImage"),
                        labelInfo.beerSupplier.as("beerSupplier"),
                        productBomEntity.productId.as("productId"),  // productBom -> productBomEntity로 변경
                        productBomEntity.productName.as("productName"),  // productBom -> productBomEntity로 변경
                        productBomEntity.sizeSpec.as("sizeSpec"),  // productBom -> productBomEntity로 변경
                        productBomEntity.alcPercent.as("alcPercent")  // productBom -> productBomEntity로 변경
                ))
                .from(labelInfo)
                .join(labelInfo.productBom, productBomEntity)  // labelInfo.productBom -> productBomEntity로 변경
                .on(labelInfo.productBom.productId.eq(productBomEntity.productId))  // productBom.productId -> productBomEntity.productId로 변경
                .where(labelInfo.labelId.eq(labelId))  // WHERE 조건 추가
                .fetchOne();  // 결과가 하나일 경우 fetchOne 사용

        return labelInfoDTO;
    }



    public void createLabelInfo(LabelInfoDTO labelInfo) {
    }

    public void updateLabelInfo(LabelInfoDTO labelInfo) {
    }

    public void deleteLabelInfo(String labelId) {
    }
}
