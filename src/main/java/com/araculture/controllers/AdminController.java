package com.araculture.controllers;

import com.araculture.models.Product;
import com.araculture.models.User;
import com.araculture.repositories.ProductRepository;
import com.araculture.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Crear producto
    @PostMapping("/products")
    public Product create(@RequestBody Product p) {
        return productRepository.save(p);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product req) {
        return productRepository.findById(id)
                .map(p -> {
                    p.setName(req.getName());
                    p.setDescription(req.getDescription());
                    p.setCategory(req.getCategory());
                    p.setPrice(req.getPrice());
                    p.setImageUrl(req.getImageUrl());
                    p.setPopular(req.isPopular());
                    p.setNewArrival(req.isNewArrival());
                    return ResponseEntity.ok(productRepository.save(p));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!productRepository.existsById(id)) return ResponseEntity.notFound().build();
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Promover a ADMIN
    @PostMapping("/users/{userId}/promote")
    public ResponseEntity<?> promote(@PathVariable Long userId) {
        User u = userRepository.findById(userId).orElseThrow();
        u.getRoles().add("ROLE_ADMIN");
        userRepository.save(u);
        return ResponseEntity.ok("Usuario promovido a ADMIN");
    }
}
