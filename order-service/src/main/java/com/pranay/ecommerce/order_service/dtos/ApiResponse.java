package com.pranay.ecommerce.order_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private T message;
    private String path;
    private int status;
    private String timestamp;

    public ApiResponse(T message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now().toString();
    }

    public ApiResponse(T message, String path, int status) {
        this.message = message;
        this.path = path;
        this.status = status;
        this.timestamp = Instant.now().toString();
    }

}
