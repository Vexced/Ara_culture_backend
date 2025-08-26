package com.araculture.services;

import com.araculture.models.*;
import com.araculture.repositories.CartItemRepository;
import com.araculture.repositories.OrderItemRepository;
import com.araculture.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service @RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    public List<Order> list(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Order checkout(User user, String receiverName, String shippingAddress) {
        List<CartItem> cart = cartItemRepository.findByUser(user);
        if (cart.isEmpty()) throw new IllegalStateException("Carrito vacío");

        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setShippingAddress(shippingAddress);
        order.setStatus(OrderStatus.PAID); // simulación de pago ok

        BigDecimal total = BigDecimal.ZERO;

        order = orderRepository.save(order);

        for (CartItem ci : cart) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice()); // ya es BigDecimal
            total = total.add(oi.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            orderItemRepository.save(oi);
        }

        order.setTotal(total);
        orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return order;
    }
}
