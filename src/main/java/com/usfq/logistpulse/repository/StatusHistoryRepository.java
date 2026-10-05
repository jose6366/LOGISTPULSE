package com.usfq.logistpulse.repository;
import com.usfq.logistpulse.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StatusHistoryRepository extends JpaRepository<StatusHistory,Long>{ List<StatusHistory> findByOrderIdOrderByChangedAtAsc(Long orderId); }
