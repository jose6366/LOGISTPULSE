package com.usfq.logistpulse.dto;
import jakarta.validation.constraints.*;
public record CreateOrderRequest(@NotBlank String customerName,@NotBlank String destination,@NotBlank String product,@NotNull @Min(1) Integer quantity) {}
