package com.comerbien.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comerbien.backend.exception.ResourceNotFoundException;
import com.comerbien.backend.model.dto.request.FoodFactRequest;
import com.comerbien.backend.model.dto.response.FoodFactResponse;
import com.comerbien.backend.model.dto.response.RecipeResponse;
import com.comerbien.backend.model.entity.FoodFact;
import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.repository.FoodFactRepository;
import com.comerbien.backend.repository.RecipeRepository;
import com.comerbien.backend.service.FoodFactService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoodFactServiceImpl implements FoodFactService {

    private final FoodFactRepository foodFactRepository;
    private final RecipeRepository recipeRepository;

    public FoodFactServiceImpl(FoodFactRepository foodFactRepository, RecipeRepository recipeRepository) {
        this.foodFactRepository = foodFactRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodFactResponse> getAllFoodFacts() {
        return foodFactRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodFactResponse> getActiveFoodFacts() {
        return foodFactRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FoodFactResponse getFoodFactById(Long id) {
        FoodFact foodFact = foodFactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodFact not found with id: " + id));
        return convertToResponse(foodFact);
    }

    @Override
    public FoodFactResponse createFoodFact(FoodFactRequest foodFactRequest) {
        FoodFact foodFact = new FoodFact();
        updateFoodFactFromRequest(foodFact, foodFactRequest);

        FoodFact savedFoodFact = foodFactRepository.save(foodFact);
        return convertToResponse(savedFoodFact);
    }

    @Override
    public FoodFactResponse updateFoodFact(Long id, FoodFactRequest foodFactRequest) {
        FoodFact foodFact = foodFactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodFact not found with id: " + id));

        updateFoodFactFromRequest(foodFact, foodFactRequest);
        FoodFact updatedFoodFact = foodFactRepository.save(foodFact);
        return convertToResponse(updatedFoodFact);
    }

    @Override
    public void deleteFoodFact(Long id) {
        FoodFact foodFact = foodFactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodFact not found with id: " + id));
        foodFactRepository.delete(foodFact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodFactResponse> getFoodFactsByCategory(MealCategory category) {
        return foodFactRepository.findByCategoryAndIsActiveTrue(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodFactResponse> searchFoodFacts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getActiveFoodFacts();
        }

        // Buscar por título, fact o keywords
        String searchTerm = query.trim().toLowerCase();

        return foodFactRepository.findByIsActiveTrue().stream()
                .filter(foodFact -> foodFact.getTitle().toLowerCase().contains(searchTerm) ||
                        foodFact.getFact().toLowerCase().contains(searchTerm) ||
                        foodFact.getKeywords().stream().anyMatch(keyword -> keyword.toLowerCase().contains(searchTerm)))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // MANTÉN solo este método que es el correcto:
    @Override
    @Transactional(readOnly = true)
    public List<FoodFactResponse> getRelevantFactsForRecipe(Long recipeId) {
        // Primero buscar facts específicos para esta receta
        List<FoodFact> relevantFacts = foodFactRepository.findRelevantFactsForRecipe(recipeId);

        // Si no hay facts específicos, buscar por categoría de la receta
        if (relevantFacts.isEmpty()) {
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId));
            relevantFacts = foodFactRepository.findRelevantFactsForCategory(recipe.getCategory());
        }

        // Si aún no hay facts, mostrar algunos aleatorios activos
        if (relevantFacts.isEmpty()) {
            relevantFacts = foodFactRepository.findRandomFacts(2);
        }

        return relevantFacts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodFactResponse> getRelevantFactsForCategory(MealCategory category) {
        return foodFactRepository.findRelevantFactsForCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void matchFoodFactsWithRecipes() {
        List<FoodFact> allFacts = foodFactRepository.findAll();
        List<Recipe> allRecipes = recipeRepository.findAll();

        for (FoodFact fact : allFacts) {
            // Limpiar relaciones existentes
            fact.getRelatedRecipes().clear();

            // Encontrar recetas que coincidan con los keywords del fact
            for (Recipe recipe : allRecipes) {
                if (matchesFoodFact(fact, recipe)) {
                    fact.getRelatedRecipes().add(recipe);
                }
            }

            foodFactRepository.save(fact);
        }
    }

    @Override
    public void autoAssignFoodFactsToRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        List<FoodFact> allActiveFacts = foodFactRepository.findByIsActiveTrue();

        for (FoodFact fact : allActiveFacts) {
            if (matchesFoodFact(fact, recipe)) {
                // Agregar relación si no existe
                if (!fact.getRelatedRecipes().contains(recipe)) {
                    fact.getRelatedRecipes().add(recipe);
                    foodFactRepository.save(fact);
                    System.out.println("✅ Auto-assigned FoodFact \"" + fact.getTitle() +
                            "\" to recipe \"" + recipe.getTitle() + "\"");
                }
            }
        }
    }

    private boolean matchesFoodFact(FoodFact foodFact, Recipe recipe) {
        // 1. Coincidir por categoría exacta
        if (foodFact.getCategory() != null && foodFact.getCategory() == recipe.getCategory()) {
            return true;
        }

        // 2. Coincidir por keywords en ingredientes
        String ingredients = String.join(" ", recipe.getIngredients()).toLowerCase();
        boolean keywordMatch = foodFact.getKeywords().stream()
                .anyMatch(keyword -> {
                    String lowerKeyword = keyword.toLowerCase();
                    return ingredients.contains(lowerKeyword) ||
                            ingredients.contains(lowerKeyword + " ") ||
                            ingredients.contains(" " + lowerKeyword);
                });

        // 3. Coincidir por tags de la receta
        boolean tagMatch = recipe.getTags() != null && foodFact.getKeywords().stream()
                .anyMatch(keyword -> recipe.getTags().stream()
                        .anyMatch(tag -> tag.name().toLowerCase().contains(keyword.toLowerCase())));

        // 4. Coincidir por título de la receta
        boolean titleMatch = foodFact.getKeywords().stream()
                .anyMatch(keyword -> recipe.getTitle().toLowerCase().contains(keyword.toLowerCase()));

        return keywordMatch || tagMatch || titleMatch;
    }

    private void updateFoodFactFromRequest(FoodFact foodFact, FoodFactRequest request) {
        foodFact.setTitle(request.getTitle());
        foodFact.setFact(request.getFact());
        foodFact.setSource(request.getSource());
        foodFact.setSourceUrl(request.getSourceUrl()); // ✅ NUEVO
        foodFact.setCategory(request.getCategory());
        foodFact.setIsActive(request.getIsActive());

        if (request.getKeywords() != null) {
            foodFact.setKeywords(request.getKeywords());
        }

        // Actualizar recetas relacionadas si se proporcionan
        if (request.getRelatedRecipeIds() != null && !request.getRelatedRecipeIds().isEmpty()) {
            List<Recipe> relatedRecipes = recipeRepository.findAllById(request.getRelatedRecipeIds());
            foodFact.setRelatedRecipes(relatedRecipes);
        }
    }

    private FoodFactResponse convertToResponse(FoodFact foodFact) {
        FoodFactResponse response = new FoodFactResponse();
        response.setId(foodFact.getId());
        response.setTitle(foodFact.getTitle());
        response.setFact(foodFact.getFact());
        response.setSource(foodFact.getSource());
        response.setSourceUrl(foodFact.getSourceUrl()); // ✅ VERIFICAR que esta línea existe
        response.setCategory(foodFact.getCategory());
        response.setKeywords(foodFact.getKeywords());
        response.setIsActive(foodFact.getIsActive());

        // Convertir recetas relacionadas
        if (foodFact.getRelatedRecipes() != null && !foodFact.getRelatedRecipes().isEmpty()) {
            List<RecipeResponse> relatedRecipes = foodFact.getRelatedRecipes().stream()
                    .map(this::convertToRecipeResponse)
                    .collect(Collectors.toList());
            response.setRelatedRecipes(relatedRecipes);
            response.setRelatedRecipesCount(relatedRecipes.size());
        } else {
            response.setRelatedRecipesCount(0);
        }

        return response;
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