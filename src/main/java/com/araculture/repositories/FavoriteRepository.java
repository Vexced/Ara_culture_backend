package com.araculture.repositories;

import com.araculture.models.Favorite;
import com.araculture.models.Product;
import com.araculture.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndProduct(User user, Product product);
    Optional<Favorite> findByUserAndProductId(User user, Long productId);
    void deleteByUserAndProduct(User user, Product product);
}
