package com.pranay.ecommerce.order_service.controller;

import com.pranay.ecommerce.order_service.dtos.CreateOrderResponse;
import com.pranay.ecommerce.order_service.dtos.OrderResponse;
import com.pranay.ecommerce.order_service.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestHeader("X-User-ID") String userId) {
        OrderResponse order = orderService.createOrder(userId);
        CreateOrderResponse orderResponse = new CreateOrderResponse("Order placed successfully!", order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
