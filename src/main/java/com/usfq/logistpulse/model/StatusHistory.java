package com.usfq.logistpulse.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity @Table(name="status_history") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatusHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private CustomerOrder order;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus fromStatus;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus toStatus;
 @Column(nullable=false) private String changedBy;
 @Column(nullable=false) private OffsetDateTime changedAt;
}
