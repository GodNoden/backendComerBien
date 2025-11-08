package com.comerbien.backend.service;

import com.comerbien.backend.model.dto.request.FoodFactRequest;
import com.comerbien.backend.model.dto.response.FoodFactResponse;
import com.comerbien.backend.model.enums.MealCategory;
import java.util.List;

public interface FoodFactService {

    List<FoodFactResponse> getAllFoodFacts();

    List<FoodFactResponse> getActiveFoodFacts();

    FoodFactResponse getFoodFactById(Long id);

    FoodFactResponse createFoodFact(FoodFactRequest foodFactRequest);

    FoodFactResponse updateFoodFact(Long id, FoodFactRequest foodFactRequest);

    void deleteFoodFact(Long id);

    List<FoodFactResponse> getFoodFactsByCategory(MealCategory category);

    List<FoodFactResponse> searchFoodFacts(String query);

    List<FoodFactResponse> getRelevantFactsForRecipe(Long recipeId);

    List<FoodFactResponse> getRelevantFactsForCategory(MealCategory category);

    void autoAssignFoodFactsToRecipe(Long recipeId);

    // Matching automático de facts con recetas
    void matchFoodFactsWithRecipes();
}