package com.araculture.services;

import com.araculture.models.CartItem;
import com.araculture.models.Product;
import com.araculture.models.User;
import com.araculture.repositories.CartItemRepository;
import com.araculture.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<CartItem> getCart(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Transactional
    public CartItem addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow();
        CartItem item = cartItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUser(user);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    return ci;
                });
        item.setQuantity(item.getQuantity() + Math.max(quantity, 1));
        return cartItemRepository.save(item);
    }

    @Transactional
    public CartItem updateQuantity(User user, Long productId, int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be >= 1");
        Product product = productRepository.findById(productId).orElseThrow();
        CartItem item = cartItemRepository.findByUserAndProduct(user, product).orElseThrow();
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeItem(User user, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        cartItemRepository.deleteByUserAndProduct(user, product);
    }

    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
