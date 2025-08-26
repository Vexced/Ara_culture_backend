/*package com.araculture.controllers;

import com.araculture.models.CartItem;
import com.araculture.models.User;
import com.araculture.repositories.UserRepository;
import com.araculture.services.CartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class UserCartController {

    private final UserRepository userRepository;
    private final CartService cartService;

    private User me(UserDetails ud) { return userRepository.findByUsername(ud.getUsername()).orElseThrow(); }

    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal UserDetails ud) {
        return cartService.getCart(me(ud));
    }

    // POST /api/user/cart/{productId}?quantity=1
    @PostMapping("/{productId}")
    public CartItem add(@AuthenticationPrincipal UserDetails ud,
                        @PathVariable Long productId,
                        @RequestParam(defaultValue = "1") int quantity) {
        return cartService.addToCart(me(ud), productId, quantity);
    }

    @PutMapping("/{productId}")
    public CartItem update(@AuthenticationPrincipal UserDetails ud,
                           @PathVariable Long productId,
                           @RequestBody UpdateQty req) {
        return cartService.updateQuantity(me(ud), productId, req.getQuantity());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> remove(@AuthenticationPrincipal UserDetails ud,
                                    @PathVariable Long productId) {
        cartService.removeItem(me(ud), productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<?> clear(@AuthenticationPrincipal UserDetails ud) {
        cartService.clearCart(me(ud));
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class UpdateQty { private int quantity; }
}
*/