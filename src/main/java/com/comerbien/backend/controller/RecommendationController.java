package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.service.RecommendationService;
import com.comerbien.backend.util.RecipeMapper;
import com.comerbien.backend.service.RecipeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:8080")
public class RecommendationController {

        private final RecommendationService recommendationService;
        private final RecipeService recipeService;

        public RecommendationController(RecommendationService recommendationService, RecipeService recipeService) {
                this.recommendationService = recommendationService;
                this.recipeService = recipeService;
        }

        @GetMapping("/personalized")
        public ResponseEntity<List<RecipeResponse>> getPersonalizedRecommendations(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(defaultValue = "10") int limit) {

                List<RecipeResponse> recommendations = recommendationService
                                .getPersonalizedRecommendations(userDetails.getUser().getId(), limit)
                                .stream()
                                .map(RecipeMapper::toResponse) // Usar el método estático
                                .collect(Collectors.toList());

                return ResponseEntity.ok(recommendations);
        }

        // @GetMapping("/breakfast")
        // public ResponseEntity<List<RecipeResponse>> getBreakfastRecommendations(
        //                 @AuthenticationPrincipal User user,
        //                 @RequestParam(defaultValue = "5") int limit) {

        //         List<RecipeResponse> recommendations = recommendationService
        //                         .getBreakfastRecommendations(user.getId(), limit)
        //                         .stream()
        //                         .map(recipeService::convertToResponse)
        //                         .collect(Collectors.toList());

        //         return ResponseEntity.ok(recommendations);
        // }

        // @GetMapping("/lunch")
        // public ResponseEntity<List<RecipeResponse>> getLunchRecommendations(
        //                 @AuthenticationPrincipal User user,
        //                 @RequestParam(defaultValue = "5") int limit) {

        //         List<RecipeResponse> recommendations = recommendationService
        //                         .getLunchRecommendations(user.getId(), limit)
        //                         .stream()
        //                         .map(recipeService::convertToResponse)
        //                         .collect(Collectors.toList());

        //         return ResponseEntity.ok(recommendations);
        // }

        // @GetMapping("/dinner")
        // public ResponseEntity<List<RecipeResponse>> getDinnerRecommendations(
        //                 @AuthenticationPrincipal User user,
        //                 @RequestParam(defaultValue = "5") int limit) {

        //         List<RecipeResponse> recommendations = recommendationService
        //                         .getDinnerRecommendations(user.getId(), limit)
        //                         .stream()
        //                         .map(recipeService::convertToResponse)
        //                         .collect(Collectors.toList());

        //         return ResponseEntity.ok(recommendations);
        // }

        // @GetMapping("/snack")
        // public ResponseEntity<List<RecipeResponse>> getSnackRecommendations(
        //                 @AuthenticationPrincipal User user,
        //                 @RequestParam(defaultValue = "5") int limit) {

        //         List<RecipeResponse> recommendations = recommendationService
        //                         .getSnackRecommendations(user.getId(), limit)
        //                         .stream()
        //                         .map(recipeService::convertToResponse)
        //                         .collect(Collectors.toList());

        //         return ResponseEntity.ok(recommendations);
        // }
}