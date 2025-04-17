package com.pranay.ecommerce.order_service.config;

import com.pranay.ecommerce.order_service.dtos.OrderResponse;
import com.pranay.ecommerce.order_service.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Tells ModelMapper how to convert List<OrderItem> to List<OrderItemDto>
        mapper.typeMap(Order.class, OrderResponse.class)
                .addMappings(m -> {
            m.map(Order::getOrderItems, OrderResponse::setItems);
        });

        return mapper;
    }
}
