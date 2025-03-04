package com.org.qualitycore.routing.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "PackagingProcessEntity")
@Table(name = "PACKAGING_AND_SHIPMENT")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PackagingAndShipment {

    @Id
    @Column(name = "PACKAGING_ID")
    private String packagingId;

    @Column(name = "LOT_NO")
    private String lotNo;

    @Column(name = "STATUS_CODE")
    private String statusCode;


}
