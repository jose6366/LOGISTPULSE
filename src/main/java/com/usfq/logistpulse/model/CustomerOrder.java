package com.usfq.logistpulse.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.*;
@Entity @Table(name="customer_orders") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String orderNumber;
 @Column(nullable=false) private String customerName;
 @Column(nullable=false) private String destination;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private OrderStatus status;
 @Column(nullable=false) private OffsetDateTime createdAt;
 @Column(nullable=false) private OffsetDateTime updatedAt;
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER) @Builder.Default
 private List<OrderItem> items=new ArrayList<>();
 public void addItem(OrderItem item){ items.add(item); item.setOrder(this); }
}
