package com.comerbien.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.entity.Favorite;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.repository.FavoriteRepository;
import com.comerbien.backend.repository.UserRepository;
import com.comerbien.backend.repository.RecipeRepository;
import com.comerbien.backend.service.FavoriteService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                             UserRepository userRepository,
                             RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<RecipeResponse> getUserFavorites(Long userId) {
        List<Recipe> favoriteRecipes = favoriteRepository.findFavoriteRecipesByUserId(userId);
        return favoriteRecipes.stream()
                .map(this::convertToRecipeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addToFavorites(Long userId, Long recipeId) {
        try {
            // Verificar si ya es favorito
            if (favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId)) {
                return true; // Ya es favorito, consideramos éxito
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

            Favorite favorite = new Favorite(user, recipe);
            favoriteRepository.save(favorite);
            System.out.println("✅ Favorite added - User: " + userId + ", Recipe: " + recipeId);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error adding favorite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeFromFavorites(Long userId, Long recipeId) {
        try {
            if (!favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId)) {
                return true; // No era favorito, consideramos éxito
            }
            
            favoriteRepository.deleteByUserIdAndRecipeId(userId, recipeId);
            System.out.println("✅ Favorite removed - User: " + userId + ", Recipe: " + recipeId);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error removing favorite: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isRecipeFavorite(Long userId, Long recipeId) {
        return favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId);
    }

    @Override
    public List<Long> getUserFavoriteRecipeIds(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(favorite -> favorite.getRecipe().getId())
                .collect(Collectors.toList());
    }

    private RecipeResponse convertToRecipeResponse(Recipe recipe) {
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
        
        return response;
    }
}