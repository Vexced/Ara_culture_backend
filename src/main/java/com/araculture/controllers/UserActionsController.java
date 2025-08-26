package com.araculture.controllers;

import com.araculture.models.*;
import com.araculture.repositories.*;
import com.araculture.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Data;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserActionsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/favorites")
    public ResponseEntity<List<Favorite>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(favoriteRepository.findByUser(user));
    }

    @PostMapping("/favorites/{productId}")
    public ResponseEntity<?> addFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable Long productId) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        var product = productRepository.findById(productId).orElseThrow();
        if (favoriteRepository.findByUserAndProductId(user, productId).isPresent()) {
            return ResponseEntity.badRequest().body("Producto ya está en favoritos");
        }
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setProduct(product);
        favoriteRepository.save(fav);
        return ResponseEntity.ok("Agregado a favoritos");
    }

    @DeleteMapping("/favorites/{productId}")
    public ResponseEntity<?> removeFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long productId) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        favoriteRepository.findByUserAndProductId(user, productId).ifPresent(favoriteRepository::delete);
        return ResponseEntity.ok("Eliminado de favoritos");
    }

    // Carrito
/**/
    @GetMapping("/cart")
    public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(cartItemRepository.findByUser(user));
    }

    @PostMapping("/cart/{productId}")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable Long productId,
                                       @RequestParam(defaultValue = "1") Integer quantity) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        var product = productRepository.findById(productId).orElseThrow();

        var cartItemOpt = cartItemRepository.findByUserAndProductId(user, productId);
        CartItem cartItem;
        if (cartItemOpt.isPresent()) {
            cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok("Producto añadido al carrito");
    }

    @PutMapping("/cart/{productId}")
    public ResponseEntity<?> updateCart(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Long productId,
                                        @RequestParam Integer quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Cantidad debe ser mayor a 0");
        }
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        var cartItemOpt = cartItemRepository.findByUserAndProductId(user, productId);
        if (cartItemOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Producto no está en el carrito");
        }
        CartItem cartItem = cartItemOpt.get();
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok("Cantidad actualizada");
    }

    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<?> removeFromCart(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable Long productId) {
        var user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        cartItemRepository.deleteByUserAndProductId(user, productId);
        return ResponseEntity.ok("Producto eliminado del carrito");
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        user.setPassword(null); // No enviar password
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody UpdateProfileRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setPhotoUrl(request.getPhotoUrl());
        userRepository.save(user);
        return ResponseEntity.ok("Perfil actualizado");
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Contraseña antigua incorrecta");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Contraseña actualizada");
    }

    @Data
    public static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String address;
        private String photoUrl;
    }

    @Data
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}
