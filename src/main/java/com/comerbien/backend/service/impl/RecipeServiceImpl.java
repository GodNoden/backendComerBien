package com.comerbien.backend.service.impl;

import org.springframework.stereotype.Service;

import com.comerbien.backend.exception.ResourceNotFoundException;
import com.comerbien.backend.model.dto.request.RecipeRequest;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.dto.response.UserResponse;
import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.repository.RecipeRepository;
import com.comerbien.backend.repository.UserRepository;
import com.comerbien.backend.service.FileStorageService;
import com.comerbien.backend.service.FoodFactService;
import com.comerbien.backend.service.RecipeService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final FoodFactService foodFactService; // ✅ Inyectar servicio

    public RecipeServiceImpl(RecipeRepository recipeRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService, FoodFactService foodFactService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.foodFactService = foodFactService;
    }

    @Override
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getPublicRecipes() {
        System.out.println("🔍 RecipeService.getPublicRecipes() called");
        List<Recipe> recipes = recipeRepository.findByIsPublicTrue();
        System.out.println("📊 Found " + recipes.size() + " public recipes in database");

        if (recipes.isEmpty()) {
            System.out.println("⚠️ No public recipes found in database");
        }

        return recipes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getUserRecipes(Long userId) {
        return recipeRepository.findByCreatedById(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getRecipesForUser(Long userId) {
        return recipeRepository.findByIsPublicTrueOrCreatedById(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
        return convertToResponse(recipe);
    }

    @Override
    public RecipeResponse createRecipe(RecipeRequest recipeRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Recipe recipe = new Recipe();
        updateRecipeFromRequest(recipe, recipeRequest);
        recipe.setCreatedBy(user);

        // Manejar upload de imagen si existe
        if (recipeRequest.getImageFile() != null && !recipeRequest.getImageFile().isEmpty()) {
            try {
                String imagePath = fileStorageService.storeFile(recipeRequest.getImageFile());
                recipe.setImage(imagePath);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la imagen: " + e.getMessage());
            }
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        // ✅ AUTO-ASIGNAR FOOD FACTS DESPUÉS DE CREAR LA RECETA
        try {
            foodFactService.autoAssignFoodFactsToRecipe(savedRecipe.getId());
            System.out.println("✅ Auto-assigned food facts to recipe: " + savedRecipe.getTitle());
        } catch (Exception e) {
            // No fallar la creación si el matching falla, solo loggear
            System.out.println(
                    "⚠️ Auto-assign food facts failed for recipe " + savedRecipe.getId() + ": " + e.getMessage());
        }

        return convertToResponse(savedRecipe);
    }

    @Override
    public RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        // Verificar que el usuario es el creador de la receta
        if (!recipe.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own recipes");
        }

        // Manejar upload de nueva imagen si existe
        if (recipeRequest.getImageFile() != null && !recipeRequest.getImageFile().isEmpty()) {
            try {
                // Eliminar imagen anterior si existe
                if (recipe.getImage() != null) {
                    fileStorageService.deleteFile(recipe.getImage());
                }
                String imagePath = fileStorageService.storeFile(recipeRequest.getImageFile());
                recipe.setImage(imagePath);
            } catch (Exception e) {
                throw new RuntimeException("Error al actualizar la imagen: " + e.getMessage());
            }
        }

        updateRecipeFromRequest(recipe, recipeRequest);
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToResponse(updatedRecipe);
    }

    @Override
    public void deleteRecipe(Long id, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        // Verificar que el usuario es el creador de la receta
        if (!recipe.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own recipes");
        }

        recipeRepository.delete(recipe);
    }

    @Override
    public List<RecipeResponse> getRecipesByCategory(MealCategory category) {
        return recipeRepository.findByCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getRecipesByDifficulty(Difficulty difficulty) {
        return recipeRepository.findByDifficulty(difficulty).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getRecipesByTag(Tag tag) {
        return recipeRepository.findByTag(tag).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> searchRecipes(String query) {
        return recipeRepository.findByTitleContainingIgnoreCase(query).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponse> getRecipesFilteredByUserPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> excludedIngredients = user.getExcludedIngredients();

        if (excludedIngredients == null || excludedIngredients.isEmpty()) {
            return getRecipesForUser(userId);
        }

        return recipeRepository.findByIngredientsNotIn(excludedIngredients).stream()
                .filter(recipe -> recipe.getIsPublic() || recipe.getCreatedBy().getId().equals(userId))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private void updateRecipeFromRequest(Recipe recipe, RecipeRequest request) {
        recipe.setTitle(request.getTitle());
        recipe.setTime(request.getTime());
        recipe.setDifficulty(request.getDifficulty());
        recipe.setImage(request.getImage());
        recipe.setCategory(request.getCategory());
        recipe.setCalories(request.getCalories());
        recipe.setProtein(request.getProtein());
        recipe.setCarbs(request.getCarbs());
        recipe.setFat(request.getFat());
        recipe.setInstructions(request.getInstructions());
        recipe.setIsPublic(request.getIsPublic());

        if (request.getTags() != null) {
            recipe.setTags(request.getTags());
        }
        if (request.getIngredients() != null) {
            recipe.setIngredients(request.getIngredients());
        }
    }

    private RecipeResponse convertToResponse(Recipe recipe) {
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
