/*package com.araculture.controllers;

import com.araculture.models.Favorite;
import com.araculture.models.User;
import com.araculture.repositories.UserRepository;
import com.araculture.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/favorites")
@RequiredArgsConstructor
public class UserFavoritesController {

    private final UserRepository userRepository;
    private final FavoriteService favoriteService;

    private User me(UserDetails ud) { return userRepository.findByUsername(ud.getUsername()).orElseThrow(); }

    @GetMapping
    public List<Favorite> list(@AuthenticationPrincipal UserDetails ud) {
        return favoriteService.list(me(ud));
    }

    @PostMapping("/{productId}")
    public Favorite add(@AuthenticationPrincipal UserDetails ud, @PathVariable Long productId) {
        return favoriteService.add(me(ud), productId);
    }

    @DeleteMapping("/{productId}")
    public void remove(@AuthenticationPrincipal UserDetails ud, @PathVariable Long productId) {
        favoriteService.remove(me(ud), productId);
    }
}
                                                 */