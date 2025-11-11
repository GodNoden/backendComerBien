package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.service.FavoriteService;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = { "http://localhost:8080", "https://comerbien.com.mx" })
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getUserFavorites(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RecipeResponse> favorites = favoriteService.getUserFavorites(userDetails.getUser().getId());
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<String> addToFavorites(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean added = favoriteService.addToFavorites(userDetails.getUser().getId(), recipeId);
        if (added) {
            return ResponseEntity.ok("Recipe added to favorites");
        } else {
            return ResponseEntity.badRequest().body("Recipe is already in favorites");
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> removeFromFavorites(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean removed = favoriteService.removeFromFavorites(userDetails.getUser().getId(), recipeId);
        if (removed) {
            return ResponseEntity.ok("Recipe removed from favorites");
        } else {
            return ResponseEntity.badRequest().body("Recipe was not in favorites");
        }
    }

    @GetMapping("/{recipeId}/status")
    public ResponseEntity<Boolean> isRecipeFavorite(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isFavorite = favoriteService.isRecipeFavorite(userDetails.getUser().getId(), recipeId);
        return ResponseEntity.ok(isFavorite);
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getUserFavoriteIds(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Long> favoriteIds = favoriteService.getUserFavoriteRecipeIds(userDetails.getUser().getId());
        return ResponseEntity.ok(favoriteIds);
    }
}
