package com.pranay.ecommerce.order_service.services;

import com.pranay.ecommerce.order_service.clients.ProductServiceClient;
import com.pranay.ecommerce.order_service.clients.UserServiceClient;
import com.pranay.ecommerce.order_service.dtos.OrderItemDto;
import com.pranay.ecommerce.order_service.dtos.OrderResponse;
import com.pranay.ecommerce.order_service.dtos.ProductResponse;
import com.pranay.ecommerce.order_service.dtos.UserResponse;
import com.pranay.ecommerce.order_service.models.CartItem;
import com.pranay.ecommerce.order_service.models.Order;
import com.pranay.ecommerce.order_service.models.OrderItem;
import com.pranay.ecommerce.order_service.models.OrderStatus;
import com.pranay.ecommerce.order_service.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final ModelMapper modelMapper;

    public OrderResponse createOrder(String userId) {
        //validate cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        UserResponse userDetails = userServiceClient.getUserDetails(userId);
        if(userDetails == null) {
            throw new RuntimeException("User doesn't exist with id: " + userId);
        }

        Order order = new Order();
        order.setUserId(userId);
        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(CartItem item : cartItems) {
            ProductResponse productDetails = productServiceClient.getProductDetails(item.getProductId());
            if(productDetails == null || productDetails.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Product not available or insufficient stock: "
                        + item.getProductId()); //cant place order
            }

            //  Deduct stock
            productServiceClient.decreaseStock(productDetails.getId(), item.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getPrice()); // Fetch live price from product service

            orderItems.add(orderItem);

            //sum up total price
            BigDecimal itemTotal = productDetails.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            System.out.println("Stock decreased for productId: " + item.getProductId() + " by quantity: " + item.getQuantity());

            log.info("Product stock updated: productId={}, deductedQuantity={}, userId={}",
                    item.getProductId(), item.getQuantity(), userId);
        }

        //save order
         order.setOrderItems(orderItems);
         order.setTotalAmount(totalAmount);
         order.setStatus(OrderStatus.PLACED); // we can also keep default as PENDING and update later

        Order savedOrder = orderRepository.save(order);

        //clear cart
        cartService.clearCart(userId);

        // Map OrderItems to DTOs
        List<OrderItemDto> orderItemDtos = savedOrder.getOrderItems()
                .stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDto.class))
                .collect(Collectors.toList());

//        return new OrderResponse(
//                savedOrder.getId(),
//                savedOrder.getTotalAmount(),
//                savedOrder.getStatus(),
//                orderItemDtos,
//                savedOrder.getCreatedAt()
//        );

        return modelMapper.map(savedOrder, OrderResponse.class);
    }
}
