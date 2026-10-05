package com.usfq.logistpulse.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_events")
public class AuditEvent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable = false) private String username;
  @Column(nullable = false) private String action;
  @Column(nullable = false) private String entityType;
  private String entityId;
  @Column(nullable = false) private OffsetDateTime occurredAt;

  public AuditEvent() {}
  public AuditEvent(String username, String action, String entityType, String entityId, OffsetDateTime occurredAt) {
    this.username = username; this.action = action; this.entityType = entityType; this.entityId = entityId; this.occurredAt = occurredAt;
  }
  public Long getId() { return id; }
  public String getUsername() { return username; }
  public String getAction() { return action; }
  public String getEntityType() { return entityType; }
  public String getEntityId() { return entityId; }
  public OffsetDateTime getOccurredAt() { return occurredAt; }
}
