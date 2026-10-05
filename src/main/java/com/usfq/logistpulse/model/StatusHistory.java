package com.usfq.logistpulse.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "status_history")
public class StatusHistory {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional = false) private CustomerOrder order;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus fromStatus;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus toStatus;
  @Column(nullable = false) private String changedBy;
  @Column(nullable = false) private OffsetDateTime changedAt;

  public StatusHistory() {}
  public StatusHistory(CustomerOrder order, OrderStatus fromStatus, OrderStatus toStatus, String changedBy, OffsetDateTime changedAt) {
    this.order = order; this.fromStatus = fromStatus; this.toStatus = toStatus; this.changedBy = changedBy; this.changedAt = changedAt;
  }
  public Long getId() { return id; }
  public CustomerOrder getOrder() { return order; }
  public void setOrder(CustomerOrder order) { this.order = order; }
  public OrderStatus getFromStatus() { return fromStatus; }
  public void setFromStatus(OrderStatus fromStatus) { this.fromStatus = fromStatus; }
  public OrderStatus getToStatus() { return toStatus; }
  public void setToStatus(OrderStatus toStatus) { this.toStatus = toStatus; }
  public String getChangedBy() { return changedBy; }
  public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
  public OffsetDateTime getChangedAt() { return changedAt; }
  public void setChangedAt(OffsetDateTime changedAt) { this.changedAt = changedAt; }
}
