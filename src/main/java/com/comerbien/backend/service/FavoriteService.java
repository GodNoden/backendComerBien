package com.comerbien.backend.service;

import com.comerbien.backend.model.dto.response.RecipeResponse;
import java.util.List;

public interface FavoriteService {
    List<RecipeResponse> getUserFavorites(Long userId);

    boolean addToFavorites(Long userId, Long recipeId);

    boolean removeFromFavorites(Long userId, Long recipeId);

    boolean isRecipeFavorite(Long userId, Long recipeId);

    List<Long> getUserFavoriteRecipeIds(Long userId);
}