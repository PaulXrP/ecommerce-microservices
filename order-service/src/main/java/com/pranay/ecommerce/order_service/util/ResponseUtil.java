package com.pranay.ecommerce.order_service.util;

import com.pranay.ecommerce.order_service.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(T message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(message, status.value()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T message) {
        return success(message, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> failure(T errorMessage, String path,
                                                             HttpStatus status) {
           return ResponseEntity.status(status)
                   .body(new ApiResponse<>(errorMessage, path, status.value()));
    }

    public static <T> ResponseEntity<ApiResponse<T>> failure(T errorMessage, HttpStatus status) {
         return failure(errorMessage, null, status);
    }
}
