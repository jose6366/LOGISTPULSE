package com.usfq.logistpulse.repository;
import com.usfq.logistpulse.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long>{ List<CustomerOrder> findAllByOrderByCreatedAtDesc(); }
