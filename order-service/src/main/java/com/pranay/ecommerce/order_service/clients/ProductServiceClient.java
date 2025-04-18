package com.pranay.ecommerce.order_service.clients;

import com.pranay.ecommerce.order_service.dtos.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/api/products/{id}")
    ProductResponse getProductDetails(@PathVariable Long id);

    @PutExchange("/api/products/decrease-stock/{productId}")
    void decreaseStock(@PathVariable("productId") Long productId,
                       @RequestParam("quantity") int quantity);
}
