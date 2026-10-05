package com.usfq.logistpulse.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity @Table(name="dispatches") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dispatch {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) private CustomerOrder order;
 @Column(nullable=false) private String assignedTo;
 @Column(nullable=false) private OffsetDateTime assignedAt;
}
