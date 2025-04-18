package com.pranay.ecommerce.order_service.exceptions;

import com.pranay.ecommerce.order_service.dtos.ApiResponse;
import com.pranay.ecommerce.order_service.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(InsufficientStockException ex,
                                                                       HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("Path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

//    @ExceptionHandler(InsufficientStockException.class)
//    public ResponseEntity<ApiResponse<String>> handleStock(InsufficientStockException ex,
//                                                           HttpServletRequest request) {
//        return ResponseUtil.failure(ex.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return ResponseUtil.failure("Something went wrong", request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
