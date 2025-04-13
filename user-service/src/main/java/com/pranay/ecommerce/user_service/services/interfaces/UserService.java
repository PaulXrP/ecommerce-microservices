package com.pranay.ecommerce.user_service.services.interfaces;

import com.pranay.ecommerce.user_service.dto.UserRequest;
import com.pranay.ecommerce.user_service.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse addUser(UserRequest userRequest);

    UserResponse findUser(Long id);

    List<UserResponse> fetchAllUsers();

    String updateUser(Long id, UserRequest updatedUserRequest);

    String deleteUser(Long id);
}
