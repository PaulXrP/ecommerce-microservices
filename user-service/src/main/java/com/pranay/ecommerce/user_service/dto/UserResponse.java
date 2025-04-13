package com.pranay.ecommerce.user_service.dto;

import com.pranay.ecommerce.user_service.models.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private List<AddressDto> addresses;
}
