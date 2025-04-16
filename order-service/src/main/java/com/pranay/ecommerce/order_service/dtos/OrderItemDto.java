package com.pranay.ecommerce.order_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class OrderItemDto {
    private Long id;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;
}
