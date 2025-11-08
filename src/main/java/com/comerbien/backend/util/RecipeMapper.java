package com.comerbien.backend.util;

import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.dto.response.UserResponse;
import com.comerbien.backend.model.entity.Recipe;

public class RecipeMapper {

    public static RecipeResponse toResponse(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.getId());
        response.setTitle(recipe.getTitle());
        response.setTime(recipe.getTime());
        response.setDifficulty(recipe.getDifficulty());
        response.setImage(recipe.getImage());
        response.setCategory(recipe.getCategory());
        response.setCalories(recipe.getCalories());
        response.setProtein(recipe.getProtein());
        response.setCarbs(recipe.getCarbs());
        response.setFat(recipe.getFat());
        response.setTags(recipe.getTags());
        response.setIngredients(recipe.getIngredients());
        response.setInstructions(recipe.getInstructions());
        response.setIsPublic(recipe.getIsPublic());

        if (recipe.getCreatedBy() != null) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(recipe.getCreatedBy().getId());
            userResponse.setUsername(recipe.getCreatedBy().getUsername());
            userResponse.setEmail(recipe.getCreatedBy().getEmail());
            response.setCreatedBy(userResponse);
        }

        return response;
    }
}