package com.usfq.logistpulse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @JsonIgnore @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "order_id") private CustomerOrder order;
  @Column(nullable = false) private String product;
  @Column(nullable = false) private Integer quantity;

  public OrderItem() {}
  public OrderItem(String product, Integer quantity) { this.product = product; this.quantity = quantity; }
  public Long getId() { return id; }
  public CustomerOrder getOrder() { return order; }
  public void setOrder(CustomerOrder order) { this.order = order; }
  public String getProduct() { return product; }
  public void setProduct(String product) { this.product = product; }
  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
