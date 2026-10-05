package com.usfq.logistpulse.controller;

import com.usfq.logistpulse.dto.CreateOrderRequest;
import com.usfq.logistpulse.model.OrderStatus;
import com.usfq.logistpulse.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
  private final OrderService service;
  public WebController(OrderService service) { this.service = service; }

  @GetMapping("/") public String home(Model model) { model.addAttribute("orders", service.orders()); return "index"; }
  @GetMapping("/orders/new") public String form() { return "order-form"; }

  @PostMapping("/orders")
  public String create(@RequestParam String customerName, @RequestParam String destination, @RequestParam String product,
                       @RequestParam Integer quantity, Authentication auth, RedirectAttributes ra) {
    try {
      var order = service.create(new CreateOrderRequest(customerName, destination, product, quantity), auth.getName());
      ra.addFlashAttribute("success", "Pedido " + order.getOrderNumber() + " creado");
    } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
    return "redirect:/";
  }

  @PostMapping("/orders/{id}/status")
  public String status(@PathVariable Long id, @RequestParam OrderStatus status, Authentication auth, RedirectAttributes ra) {
    try { service.changeStatus(id, status, auth.getName()); ra.addFlashAttribute("success", "Estado actualizado"); }
    catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
    return "redirect:/orders/" + id;
  }

  @PostMapping("/orders/{id}/assign")
  public String assign(@PathVariable Long id, @RequestParam String assignee, Authentication auth, RedirectAttributes ra) {
    try { service.assign(id, assignee, auth.getName()); ra.addFlashAttribute("success", "Despacho asignado"); }
    catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
    return "redirect:/orders/" + id;
  }

  @GetMapping("/orders/{id}")
  public String detail(@PathVariable Long id, Model model) {
    var order = service.order(id).orElseThrow();
    model.addAttribute("order", order);
    model.addAttribute("history", service.history(id));
    model.addAttribute("statuses", service.allowedTargets(order.getStatus()));
    model.addAttribute("canAssign", order.getStatus() == OrderStatus.READY);
    return "detail";
  }
}
