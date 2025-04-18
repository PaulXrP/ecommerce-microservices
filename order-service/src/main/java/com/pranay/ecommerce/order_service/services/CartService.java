package com.pranay.ecommerce.order_service.services;

import com.pranay.ecommerce.order_service.clients.ProductServiceClient;
import com.pranay.ecommerce.order_service.clients.UserServiceClient;
import com.pranay.ecommerce.order_service.dtos.CartItemRequest;
import com.pranay.ecommerce.order_service.dtos.ProductResponse;
import com.pranay.ecommerce.order_service.dtos.UserResponse;
import com.pranay.ecommerce.order_service.models.CartItem;
import com.pranay.ecommerce.order_service.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public boolean addToCart(String userId, CartItemRequest request) {
       //Look for product
        ProductResponse productDetails = productServiceClient.getProductDetails(request.getProductId());
        if(productDetails == null || productDetails.getStockQuantity() < request.getQuantity())
            return false;

        UserResponse userDetails = userServiceClient.getUserDetails(userId);
        if(userDetails == null || userDetails.getId()==null) {
            log.info("User details: {}", userDetails);
            return false;
        }

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem != null) {
            //update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            existingCartItem.setPrice(productDetails.getPrice());

            cartItemRepository.save(existingCartItem);
        } else {
            //create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
//          cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItem.setPrice(productDetails.getPrice());
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
