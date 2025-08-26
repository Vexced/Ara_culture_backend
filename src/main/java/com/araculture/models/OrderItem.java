package com.araculture.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity @Table(name="order_items")
@Getter @Setter @NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    private int quantity;

    // precio al momento de la compra (snapshot)
    private BigDecimal price;
}
