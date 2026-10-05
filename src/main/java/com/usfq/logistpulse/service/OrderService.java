package com.usfq.logistpulse.service;

import com.usfq.logistpulse.dto.CreateOrderRequest;
import com.usfq.logistpulse.model.*;
import com.usfq.logistpulse.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class OrderService {
  private final OrderRepository orders;
  private final StatusHistoryRepository history;
  private final DispatchRepository dispatches;
  private final AuditEventRepository audits;

  public OrderService(OrderRepository orders, StatusHistoryRepository history, DispatchRepository dispatches, AuditEventRepository audits) {
    this.orders = orders; this.history = history; this.dispatches = dispatches; this.audits = audits;
  }

  public List<CustomerOrder> orders() { return orders.findAllByOrderByCreatedAtDesc(); }
  public Optional<CustomerOrder> order(Long id) { return orders.findById(id); }
  public List<StatusHistory> history(Long id) { return history.findByOrderIdOrderByChangedAtAsc(id); }

  public List<OrderStatus> allowedTargets(OrderStatus from) {
    return switch (from) {
      case CREATED -> List.of(OrderStatus.READY, OrderStatus.CANCELLED);
      case READY -> List.of(OrderStatus.DISPATCHED, OrderStatus.CANCELLED);
      case DISPATCHED -> List.of(OrderStatus.DELIVERED);
      default -> List.of();
    };
  }

  @Transactional
  public CustomerOrder create(CreateOrderRequest req, String username) {
    if (req == null || req.customerName() == null || req.customerName().isBlank()) throw new IllegalArgumentException("Cliente requerido");
    if (req.destination() == null || req.destination().isBlank()) throw new IllegalArgumentException("Destino requerido");
    if (req.product() == null || req.product().isBlank()) throw new IllegalArgumentException("Producto requerido");
    if (req.quantity() == null || req.quantity() < 1) throw new IllegalArgumentException("Cantidad debe ser mayor que cero");

    OffsetDateTime now = OffsetDateTime.now();
    CustomerOrder order = new CustomerOrder();
    order.setOrderNumber("LP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    order.setCustomerName(req.customerName().trim());
    order.setDestination(req.destination().trim());
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(now);
    order.setUpdatedAt(now);
    order.addItem(new OrderItem(req.product().trim(), req.quantity()));
    order = orders.save(order);

    history.save(new StatusHistory(order, OrderStatus.CREATED, OrderStatus.CREATED, username, now));
    audits.save(new AuditEvent(username, "ORDER_CREATED", "CustomerOrder", order.getId().toString(), now));
    return order;
  }

  @Transactional
  public CustomerOrder changeStatus(Long id, OrderStatus target, String username) {
    CustomerOrder order = orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    OrderStatus from = order.getStatus();
    if (!allowedTargets(from).contains(target)) throw new IllegalArgumentException("Transición de estado no permitida: " + from + " -> " + target);

    OffsetDateTime now = OffsetDateTime.now();
    order.setStatus(target);
    order.setUpdatedAt(now);
    orders.save(order);
    history.save(new StatusHistory(order, from, target, username, now));
    audits.save(new AuditEvent(username, "ORDER_STATUS_CHANGED", "CustomerOrder", order.getId().toString(), now));
    return order;
  }

  @Transactional
  public Dispatch assign(Long id, String assignee, String username) {
    if (assignee == null || assignee.isBlank()) throw new IllegalArgumentException("Responsable requerido");
    CustomerOrder order = orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    if (order.getStatus() != OrderStatus.READY) throw new IllegalArgumentException("Solo pedidos READY pueden asignarse");

    changeStatus(id, OrderStatus.DISPATCHED, username);
    Dispatch dispatch = dispatches.save(new Dispatch(order, assignee.trim(), OffsetDateTime.now()));
    audits.save(new AuditEvent(username, "DISPATCH_ASSIGNED", "CustomerOrder", order.getId().toString(), OffsetDateTime.now()));
    return dispatch;
  }
}
