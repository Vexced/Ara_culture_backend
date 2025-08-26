package com.araculture.repositories;

import com.araculture.models.CartItem;
import com.araculture.models.Product;
import com.araculture.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProductId(User user, Long productId);
    Optional<CartItem> findByUserAndProduct(User user, Product product);


    void deleteByUserAndProductId(User user, Long productId);
    void deleteByUser(User user);
    void deleteByUserAndProduct(User user, Product product);

}
