package com.usfq.logistpulse.service;

import com.usfq.logistpulse.dto.CreateOrderRequest;
import com.usfq.logistpulse.model.OrderStatus;
import com.usfq.logistpulse.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {
  @Autowired OrderService service;
  @Autowired OrderRepository orders;
  @Autowired StatusHistoryRepository history;
  @Autowired DispatchRepository dispatches;
  @Autowired AuditEventRepository audits;

  @BeforeEach
  void setup() {
    dispatches.deleteAll(); history.deleteAll(); audits.deleteAll(); orders.deleteAll();
  }

  @Test
  void createsOrderItemHistoryAndAudit() {
    var order = service.create(new CreateOrderRequest("Sucursal", "Quito", "Caja", 2), "operator");
    assertNotNull(order.getId());
    assertEquals(OrderStatus.CREATED, order.getStatus());
    assertEquals(1, order.getItems().size());
    assertEquals(1, service.history(order.getId()).size());
    assertEquals(1, audits.count());
  }

  @Test
  void acceptsValidAndRejectsInvalidTransition() {
    var order = service.create(new CreateOrderRequest("Sucursal", "Quito", "Caja", 2), "operator");
    service.changeStatus(order.getId(), OrderStatus.READY, "operator");
    assertEquals(OrderStatus.READY, service.order(order.getId()).orElseThrow().getStatus());
    assertThrows(IllegalArgumentException.class, () -> service.changeStatus(order.getId(), OrderStatus.DELIVERED, "operator"));
  }

  @Test
  void assignsReadyOrderAndMovesToDispatched() {
    var order = service.create(new CreateOrderRequest("Sucursal", "Quito", "Caja", 2), "operator");
    service.changeStatus(order.getId(), OrderStatus.READY, "operator");
    var dispatch = service.assign(order.getId(), "Camión 1", "operator");
    assertNotNull(dispatch.getId());
    assertEquals("Camión 1", dispatch.getAssignedTo());
    assertEquals(OrderStatus.DISPATCHED, service.order(order.getId()).orElseThrow().getStatus());
  }
}
