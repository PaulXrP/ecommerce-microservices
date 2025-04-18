package com.pranay.ecommerce.order_service.clients;

import com.pranay.ecommerce.order_service.dtos.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserServiceClient {

    @GetExchange("/api/user/find/{id}")
    UserResponse getUserDetails(@PathVariable String id);
}
