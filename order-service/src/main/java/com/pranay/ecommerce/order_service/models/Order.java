package com.pranay.ecommerce.order_service.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id") // establishes FK in OrderItem table
    private List<OrderItem> orderItems = new ArrayList<>();


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
