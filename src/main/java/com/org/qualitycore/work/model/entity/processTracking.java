    package com.org.qualitycore.work.model.entity;

    import jakarta.persistence.*;
    import lombok.*;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @Entity
    @Table(name = "PROCESS_TRACKING")
    public class processTracking {

        @Id
        @Column(name = "TRACKING_ID")
        private String trackingId;

        @OneToOne(mappedBy = "processTracking")
        private WorkOrders workOrders;

        @Column(name = "STATUS_CODE")
        private String statusCode;

        @Column(name = "PROCESS_STATUS")
        private String processStatus;

        @Column(name = "PROCESS_NAME")
        private String processName;

    }
