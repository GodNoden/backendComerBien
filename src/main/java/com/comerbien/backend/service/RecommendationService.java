package com.comerbien.backend.service;

import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.model.enums.MealCategory;

import java.util.List;

public interface RecommendationService {

    List<Recipe> getRecommendedRecipes(Long userId, MealCategory category, int limit);

    List<Recipe> getBreakfastRecommendations(Long userId, int limit);

    List<Recipe> getLunchRecommendations(Long userId, int limit);

    List<Recipe> getDinnerRecommendations(Long userId, int limit);

    List<Recipe> getSnackRecommendations(Long userId, int limit);

    List<Recipe> getPersonalizedRecommendations(Long userId, int limit);
}