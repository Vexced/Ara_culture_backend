package com.araculture.controllers;

import com.araculture.models.*;
import com.araculture.repositories.*;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails userDetails,
                                      @Valid @RequestBody CheckoutRequest request) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("El carrito está vacío");
        }

        // Crear items de pedido
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            OrderItem oi = new OrderItem();
            oi.setProduct(p);
            oi.setQuantity(ci.getQuantity());
            orderItems.add(oi);

            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        // Crear pedido
        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotal(total);
        order.setStatus(request.getStatus());
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // Vaciar carrito
        cartItemRepository.deleteAll(cartItems);

        // Aquí iría integración real con pasarela de pago, pero solo simulamos
        return ResponseEntity.ok("Pago simulado exitoso. Pedido creado con id: " + order.getId());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(orders);
    }

    @Data
    public static class CheckoutRequest {
        private String shippingAddress;
        private String recipientName;
        private String cardNumber;
        private String cardExpiration;
        private String cardCvv;
        private OrderStatus status;
    }
}
