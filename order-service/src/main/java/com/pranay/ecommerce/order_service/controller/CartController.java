package com.pranay.ecommerce.order_service.controller;

import com.pranay.ecommerce.order_service.dtos.CartItemRequest;
import com.pranay.ecommerce.order_service.models.CartItem;
import com.pranay.ecommerce.order_service.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                            @RequestBody CartItemRequest request) {
          if(!cartService.addToCart(userId, request)) {
              return ResponseEntity
                      .badRequest()
                      .body("Product out of stock, or user/product not found");
          }
          return ResponseEntity.status(HttpStatus.CREATED).body(" Product added to cart");
    }

    @GetMapping()
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId) {
        List<CartItem> cartItems = cartService.getCart(userId);
        return new ResponseEntity<>(cartItems, HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable Long productId) {
        boolean deleteItemFromCart = cartService.deleteItemFromCart(userId, productId);
        return deleteItemFromCart ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
