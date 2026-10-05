package com.usfq.logistpulse.repository;
import com.usfq.logistpulse.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {}
