package com.usfq.logistpulse.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "dispatches")
public class Dispatch {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @OneToOne(optional = false) private CustomerOrder order;
  @Column(nullable = false) private String assignedTo;
  @Column(nullable = false) private OffsetDateTime assignedAt;

  public Dispatch() {}
  public Dispatch(CustomerOrder order, String assignedTo, OffsetDateTime assignedAt) { this.order = order; this.assignedTo = assignedTo; this.assignedAt = assignedAt; }
  public Long getId() { return id; }
  public CustomerOrder getOrder() { return order; }
  public void setOrder(CustomerOrder order) { this.order = order; }
  public String getAssignedTo() { return assignedTo; }
  public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
  public OffsetDateTime getAssignedAt() { return assignedAt; }
  public void setAssignedAt(OffsetDateTime assignedAt) { this.assignedAt = assignedAt; }
}
