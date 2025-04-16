package com.pranay.ecommerce.order_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity()
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private Integer quantity;
    private BigDecimal unitPrice;

//    @ManyToOne
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
}
