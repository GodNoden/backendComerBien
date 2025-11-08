package com.comerbien.backend.service;

import com.comerbien.backend.model.dto.request.RecipeRequest;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.Tag;
import java.util.List;

public interface RecipeService {
    List<RecipeResponse> getAllRecipes();

    List<RecipeResponse> getPublicRecipes();

    List<RecipeResponse> getUserRecipes(Long userId);

    List<RecipeResponse> getRecipesForUser(Long userId); // Públicas + del usuario

    RecipeResponse getRecipeById(Long id);

    RecipeResponse createRecipe(RecipeRequest recipeRequest, Long userId);

    RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest, Long userId);

    void deleteRecipe(Long id, Long userId);

    List<RecipeResponse> getRecipesByCategory(MealCategory category);

    List<RecipeResponse> getRecipesByDifficulty(Difficulty difficulty);

    List<RecipeResponse> getRecipesByTag(Tag tag);

    List<RecipeResponse> searchRecipes(String query);

    List<RecipeResponse> getRecipesFilteredByUserPreferences(Long userId);
    
}