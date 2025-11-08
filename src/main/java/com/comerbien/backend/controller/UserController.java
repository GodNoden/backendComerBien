package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.comerbien.backend.model.dto.request.UserPreferencesRequest;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.dto.response.UserResponse;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        System.out.println("🔍 GET /profile called");
        System.out.println("   - userDetails: " + (userDetails != null ? "EXISTS" : "NULL"));

        if (userDetails == null) {
            System.out.println("❌ userDetails is null - returning 401");
            return ResponseEntity.status(401).build();
        }

        System.out.println("   - User ID: " + userDetails.getUser().getId());

        try {
            UserResponse user = userService.getUserProfile(userDetails.getUser().getId());
            System.out.println("✅ User profile found: " + (user != null ? "EXISTS" : "NULL"));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.out.println("❌ Error getting user profile: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateUserProfile(
            @RequestBody UserResponse userResponse,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse updatedUser = userService.updateUserProfile(userDetails.getUser().getId(), userResponse);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/dietary-preferences")
    public ResponseEntity<UserResponse> updateDietaryPreferences(
            @RequestBody List<Tag> dietaryPreferences,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse updatedUser = userService.updateDietaryPreferences(userDetails.getUser().getId(),
                dietaryPreferences);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/excluded-ingredients")
    public ResponseEntity<UserResponse> updateExcludedIngredients(
            @RequestBody List<String> excludedIngredients,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse updatedUser = userService.updateExcludedIngredients(userDetails.getUser().getId(),
                excludedIngredients);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/preferences")
    public ResponseEntity<UserResponse> updateUserPreferences(
            @AuthenticationPrincipal CustomUserDetails userDetails, // CAMBIAR A User → CustomUserDetails
            @RequestBody UserPreferencesRequest preferences) {

        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        UserResponse updatedUser = userService.updateUserPreferences(userDetails.getUser().getId(), preferences);
        return ResponseEntity.ok(updatedUser);
    }

    // @GetMapping("/personalized")
    // public ResponseEntity<List<RecipeResponse>> getPersonalizedRecommendations(
    // @AuthenticationPrincipal CustomUserDetails userDetails,
    // @RequestParam(defaultValue = "10") int limit) {

    // List<RecipeResponse> recommendations = recommendationService
    // .getPersonalizedRecommendations(userDetails.getUser().getId(), limit)
    // .stream()
    // .map(recipeService::convertToResponse)
    // .collect(Collectors.toList());

    // return ResponseEntity.ok(recommendations);
    // }
}