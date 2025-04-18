package com.pranay.ecommerce.order_service.dtos;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
