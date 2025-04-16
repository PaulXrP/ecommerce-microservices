package com.pranay.ecommerce.order_service.dtos;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDto addressDto;
}
