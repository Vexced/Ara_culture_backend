package com.araculture.services;

import com.araculture.models.Favorite;
import com.araculture.models.Product;
import com.araculture.models.User;
import com.araculture.repositories.FavoriteRepository;
import com.araculture.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    public List<Favorite> list(User user) {
        return favoriteRepository.findByUser(user);
    }

    public Favorite add(User user, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return favoriteRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    Favorite f = new Favorite();
                    f.setUser(user);
                    f.setProduct(product);
                    return favoriteRepository.save(f);
                });
    }

    public void remove(User user, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        favoriteRepository.deleteByUserAndProduct(user, product);
    }
}
