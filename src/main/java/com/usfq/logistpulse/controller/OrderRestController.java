package com.usfq.logistpulse.controller;
import com.usfq.logistpulse.dto.CreateOrderRequest;
import com.usfq.logistpulse.model.*;
import com.usfq.logistpulse.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/orders")
public class OrderRestController {
 private final OrderService service; public OrderRestController(OrderService service){this.service=service;}
 @GetMapping public List<CustomerOrder> list(){return service.orders();}
 @GetMapping("/{id}") public ResponseEntity<CustomerOrder> get(@PathVariable Long id){return service.order(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());}
 @PostMapping public ResponseEntity<?> create(@Valid @RequestBody CreateOrderRequest req,Authentication auth){try{return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req,auth.getName()));}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}}
 @PostMapping("/{id}/status/{status}") public ResponseEntity<?> status(@PathVariable Long id,@PathVariable OrderStatus status,Authentication auth){try{return ResponseEntity.ok(service.changeStatus(id,status,auth.getName()));}catch(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}}
 @GetMapping("/{id}/history") public List<StatusHistory> history(@PathVariable Long id){return service.history(id);}
}
