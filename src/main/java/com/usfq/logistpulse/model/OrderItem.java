package com.usfq.logistpulse.model;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity @Table(name="order_items") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="order_id") private CustomerOrder order;
 @Column(nullable=false) private String product;
 @Column(nullable=false) private Integer quantity;
}
