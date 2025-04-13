package com.pranay.ecommerce.user_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<AddressDto> addresses;
}
