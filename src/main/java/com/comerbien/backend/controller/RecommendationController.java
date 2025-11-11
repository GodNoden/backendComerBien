package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.service.RecommendationService;
import com.comerbien.backend.util.RecipeMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = { "http://localhost:8080", "https://comerbien.com.mx" })
public class RecommendationController {

        private final RecommendationService recommendationService;

        public RecommendationController(RecommendationService recommendationService) {
                this.recommendationService = recommendationService;
        }

        @GetMapping("/personalized")
        public ResponseEntity<List<RecipeResponse>> getPersonalizedRecommendations(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(defaultValue = "10") int limit) {

                try {
                        if (userDetails == null) {
                                System.err.println("❌ RecommendationController: UserDetails is null");
                                return ResponseEntity.badRequest().build();
                        }

                        Long userId = userDetails.getUser().getId();
                        System.out.println(
                                        "🎯 RecommendationController: Getting recommendations for user ID: " + userId);

                        List<RecipeResponse> recommendations = recommendationService
                                        .getPersonalizedRecommendations(userId, limit)
                                        .stream()
                                        .map(RecipeMapper::toResponse)
                                        .collect(Collectors.toList());

                        System.out.println("✅ RecommendationController: Successfully returned " +
                                        recommendations.size() + " recommendations for user " + userId);

                        return ResponseEntity.ok(recommendations);

                } catch (Exception e) {
                        System.err.println("❌ RecommendationController Error: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/debug")
        public ResponseEntity<String> debugRecommendations(@AuthenticationPrincipal CustomUserDetails userDetails) {
                try {
                        if (userDetails == null) {
                                return ResponseEntity.ok("UserDetails is null - user not authenticated");
                        }

                        Long userId = userDetails.getUser().getId();
                        var user = userDetails.getUser();

                        String debugInfo = String.format(
                                        "🔍 DEBUG INFO - User ID: %d\n" +
                                                        "Username: %s\n" +
                                                        "Allergies: %s\n" +
                                                        "Disliked Ingredients: %s\n" +
                                                        "Nutritional Preferences: %s\n" +
                                                        "Weight Goal: %s\n" +
                                                        "Age: %d, Height: %d, Weight: %d",
                                        userId,
                                        user.getUsername(),
                                        user.getAllergies(),
                                        user.getDislikedIngredients(),
                                        user.getNutritionalPreferences(),
                                        user.getWeightGoal(),
                                        user.getAge(),
                                        user.getHeight(),
                                        user.getWeight());

                        System.out.println(debugInfo);
                        return ResponseEntity.ok(debugInfo);

                } catch (Exception e) {
                        return ResponseEntity.ok("Debug error: " + e.getMessage());
                }
        }

        // @GetMapping("/breakfast")
        // public ResponseEntity<List<RecipeResponse>> getBreakfastRecommendations(
        // @AuthenticationPrincipal User user,
        // @RequestParam(defaultValue = "5") int limit) {

        // List<RecipeResponse> recommendations = recommendationService
        // .getBreakfastRecommendations(user.getId(), limit)
        // .stream()
        // .map(recipeService::convertToResponse)
        // .collect(Collectors.toList());

        // return ResponseEntity.ok(recommendations);
        // }

        // @GetMapping("/lunch")
        // public ResponseEntity<List<RecipeResponse>> getLunchRecommendations(
        // @AuthenticationPrincipal User user,
        // @RequestParam(defaultValue = "5") int limit) {

        // List<RecipeResponse> recommendations = recommendationService
        // .getLunchRecommendations(user.getId(), limit)
        // .stream()
        // .map(recipeService::convertToResponse)
        // .collect(Collectors.toList());

        // return ResponseEntity.ok(recommendations);
        // }

        // @GetMapping("/dinner")
        // public ResponseEntity<List<RecipeResponse>> getDinnerRecommendations(
        // @AuthenticationPrincipal User user,
        // @RequestParam(defaultValue = "5") int limit) {

        // List<RecipeResponse> recommendations = recommendationService
        // .getDinnerRecommendations(user.getId(), limit)
        // .stream()
        // .map(recipeService::convertToResponse)
        // .collect(Collectors.toList());

        // return ResponseEntity.ok(recommendations);
        // }

        // @GetMapping("/snack")
        // public ResponseEntity<List<RecipeResponse>> getSnackRecommendations(
        // @AuthenticationPrincipal User user,
        // @RequestParam(defaultValue = "5") int limit) {

        // List<RecipeResponse> recommendations = recommendationService
        // .getSnackRecommendations(user.getId(), limit)
        // .stream()
        // .map(recipeService::convertToResponse)
        // .collect(Collectors.toList());

        // return ResponseEntity.ok(recommendations);
        // }
}