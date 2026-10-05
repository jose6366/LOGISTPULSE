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
 private final OrderRepository orders; private final StatusHistoryRepository history; private final DispatchRepository dispatches;
 public OrderService(OrderRepository orders,StatusHistoryRepository history,DispatchRepository dispatches){this.orders=orders;this.history=history;this.dispatches=dispatches;}
 public List<CustomerOrder> orders(){return orders.findAllByOrderByCreatedAtDesc();}
 public Optional<CustomerOrder> order(Long id){return orders.findById(id);}
 public List<StatusHistory> history(Long id){return history.findByOrderIdOrderByChangedAtAsc(id);}
 @Transactional public CustomerOrder create(CreateOrderRequest req,String username){
   var now=OffsetDateTime.now(); var order=CustomerOrder.builder().orderNumber("LP-"+UUID.randomUUID().toString().substring(0,8).toUpperCase()).customerName(req.customerName().trim()).destination(req.destination().trim()).status(OrderStatus.CREATED).createdAt(now).updatedAt(now).build();
   order.addItem(OrderItem.builder().product(req.product().trim()).quantity(req.quantity()).build()); order=orders.save(order);
   history.save(StatusHistory.builder().order(order).fromStatus(OrderStatus.CREATED).toStatus(OrderStatus.CREATED).changedBy(username).changedAt(now).build()); return order;
 }
 @Transactional public CustomerOrder changeStatus(Long id,OrderStatus target,String username){
   var order=orders.findById(id).orElseThrow(()->new IllegalArgumentException("Pedido no encontrado")); var from=order.getStatus();
   if(!allowed(from,target)) throw new IllegalArgumentException("Transición de estado no permitida: "+from+" -> "+target);
   order.setStatus(target); order.setUpdatedAt(OffsetDateTime.now()); orders.save(order);
   history.save(StatusHistory.builder().order(order).fromStatus(from).toStatus(target).changedBy(username).changedAt(OffsetDateTime.now()).build()); return order;
 }
 @Transactional public Dispatch assign(Long id,String assignee,String username){
   var order=orders.findById(id).orElseThrow(()->new IllegalArgumentException("Pedido no encontrado"));
   if(order.getStatus()!=OrderStatus.READY) throw new IllegalArgumentException("Solo pedidos READY pueden asignarse");
   changeStatus(id,OrderStatus.DISPATCHED,username); return dispatches.save(Dispatch.builder().order(order).assignedTo(assignee).assignedAt(OffsetDateTime.now()).build());
 }
 private boolean allowed(OrderStatus from,OrderStatus to){ return switch(from){ case CREATED -> to==OrderStatus.READY||to==OrderStatus.CANCELLED; case READY -> to==OrderStatus.DISPATCHED||to==OrderStatus.CANCELLED; case DISPATCHED -> to==OrderStatus.DELIVERED; default -> false; }; }
}
