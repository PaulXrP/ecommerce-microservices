package com.pranay.ecommerce.user_service.services.impl;

import com.pranay.ecommerce.user_service.dto.AddressDto;
import com.pranay.ecommerce.user_service.dto.UserRequest;
import com.pranay.ecommerce.user_service.dto.UserResponse;
import com.pranay.ecommerce.user_service.models.Address;
import com.pranay.ecommerce.user_service.models.User;
import com.pranay.ecommerce.user_service.repository.UserRepository;
import com.pranay.ecommerce.user_service.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;


    public UserResponse addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserResponse.class);
    }

    public UserResponse findUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User with given id not found..."));

        return modelMapper.map(user, UserResponse.class);
    }

    public List<UserResponse> fetchAllUsers() {
        List<User> all = userRepository.findAll();

        return all.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public String updateUser(Long id, UserRequest updatedUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User with given id not found...."));
        updateUserFromRequest(user, updatedUserRequest);
        userRepository.save(user);
        return "User updated successfully....";
    }

    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully from database";
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

//        if (userRequest.getAddresses() != null) {
//            List<Address> addressList = new ArrayList<>();
//            for (AddressDto dto : userRequest.getAddresses()) {
//                Address address = new Address();
//                address.setStreet(dto.getStreet());
//                address.setCity(dto.getCity());
//                address.setState(dto.getState());
//                address.setCountry(dto.getCountry());
//                address.setZipCode(dto.getZipCode());
//                address.setUser(user); // Important for bidirectional mapping
//                addressList.add(address);
//            }
//            user.setAddresses(addressList);
//        }

        if(userRequest.getAddresses() != null) {
            List<Address> addressList = userRequest.getAddresses().stream()
                    .map(dto -> {
                        Address address = new Address();
                        address.setCity(dto.getCity());
                        address.setState(dto.getState());
                        address.setZipCode(dto.getZipCode());
                        address.setStreet(dto.getStreet());
                        address.setCountry(dto.getCountry());
                        address.setUser(user); //  Important for bidirectional mapping
                        return address;
                    }).collect(Collectors.toList());

            user.getAddresses().clear();
            user.getAddresses().addAll(addressList);
        }
    }
}
