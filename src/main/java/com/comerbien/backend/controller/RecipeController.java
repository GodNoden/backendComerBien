package com.comerbien.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.comerbien.backend.model.dto.request.RecipeRequest;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.service.RecipeService;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "http://localhost:8080")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAllRecipes() {
        List<RecipeResponse> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/public")
    public ResponseEntity<List<RecipeResponse>> getPublicRecipes() {
        System.out.println("🎯🎯🎯 GET /api/recipes/public ENDPOINT CALLED 🎯🎯🎯");
        System.out.println("✅ This should work now with CORS fixed");

        try {
            List<RecipeResponse> recipes = recipeService.getPublicRecipes();
            System.out.println("📊 Returning " + recipes.size() + " public recipes");
            return ResponseEntity.ok(recipes);
        } catch (Exception e) {
            System.out.println("❌ Error in /recipes/public: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<RecipeResponse>> getUserRecipes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RecipeResponse> recipes = recipeService.getUserRecipes(userDetails.getUser().getId());
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/available")
    public ResponseEntity<List<RecipeResponse>> getAvailableRecipes(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RecipeResponse> recipes = recipeService.getRecipesForUser(userDetails.getUser().getId());
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable Long id) {
        RecipeResponse recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecipeResponse> createRecipe(
            @RequestPart("recipe") RecipeRequest recipeRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (imageFile != null && !imageFile.isEmpty()) {
            recipeRequest.setImageFile(imageFile);
        }

        RecipeResponse recipe = recipeService.createRecipe(recipeRequest, userDetails.getUser().getId());
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeRequest recipeRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        RecipeResponse recipe = recipeService.updateRecipe(id, recipeRequest, userDetails.getUser().getId());
        return ResponseEntity.ok(recipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        recipeService.deleteRecipe(id, userDetails.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<RecipeResponse>> getRecipesByCategory(@PathVariable MealCategory category) {
        List<RecipeResponse> recipes = recipeService.getRecipesByCategory(category);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<RecipeResponse>> getRecipesByDifficulty(@PathVariable Difficulty difficulty) {
        List<RecipeResponse> recipes = recipeService.getRecipesByDifficulty(difficulty);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<RecipeResponse>> getRecipesByTag(@PathVariable Tag tag) {
        List<RecipeResponse> recipes = recipeService.getRecipesByTag(tag);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponse>> searchRecipes(@RequestParam String q) {
        List<RecipeResponse> recipes = recipeService.searchRecipes(q);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<RecipeResponse>> getFilteredRecipes(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<RecipeResponse> recipes = recipeService.getRecipesFilteredByUserPreferences(userDetails.getUser().getId());
        return ResponseEntity.ok(recipes);
    }
}