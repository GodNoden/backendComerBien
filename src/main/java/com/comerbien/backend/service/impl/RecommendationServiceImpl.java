package com.comerbien.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.model.enums.WeightGoal;
import com.comerbien.backend.repository.RecipeRepository;
import com.comerbien.backend.repository.UserRepository;
import com.comerbien.backend.service.RecommendationService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecommendationServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Recipe> getRecommendedRecipes(Long userId, MealCategory category, int limit) {
        System.out.println("🎯 ===== INICIANDO RECOMENDACIONES =====");
        System.out.println("👤 User ID: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // LOG DETALLADO DE PREFERENCIAS
        System.out.println("📊 PREFERENCIAS DEL USUARIO:");
        System.out.println("   - WeightGoal: " + user.getWeightGoal());
        System.out.println("   - Age: " + user.getAge());
        System.out.println("   - Height: " + user.getHeight());
        System.out.println("   - Weight: " + user.getWeight());
        System.out.println("   - Gender: " + user.getGender());
        System.out.println("   - ActivityLevel: " + user.getActivityLevel());
        System.out.println("   - RecommendedCalories: " + user.getRecommendedCalories());
        System.out.println("   - Allergies: " + user.getAllergies());
        System.out.println("   - DislikedIngredients: " + user.getDislikedIngredients());
        System.out.println("   - NutritionalPreferences: " + user.getNutritionalPreferences());

        List<Recipe> allRecipes = recipeRepository.findByIsPublicTrueOrCreatedById(userId);
        System.out.println("📚 Total recetas disponibles: " + allRecipes.size());

        // Aplicar árbol de decisión
        List<Recipe> filteredRecipes = applyDecisionTree(user, allRecipes, category);
        System.out.println("✅ Recetas después del filtro: " + filteredRecipes.size());

        // LOG DEL SCORING
        System.out.println("🏆 TOP 5 RECETAS RECOMENDADAS:");
        filteredRecipes.sort((r1, r2) -> {
            double score1 = calculateRecommendationScore(user, r1);
            double score2 = calculateRecommendationScore(user, r2);
            return Double.compare(score2, score1);
        });

        for (int i = 0; i < Math.min(5, filteredRecipes.size()); i++) {
            Recipe recipe = filteredRecipes.get(i);
            double score = calculateRecommendationScore(user, recipe);
            System.out.println("   " + (i + 1) + ". " + recipe.getTitle() +
                    " | Cal: " + recipe.getCalories() +
                    " | Score: " + score);
        }

        System.out.println("🎯 ===== FIN RECOMENDACIONES =====");

        return filteredRecipes.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<Recipe> getBreakfastRecommendations(Long userId, int limit) {
        return getRecommendedRecipes(userId, MealCategory.DESAYUNO, limit);
    }

    @Override
    public List<Recipe> getLunchRecommendations(Long userId, int limit) {
        return getRecommendedRecipes(userId, MealCategory.COMIDA, limit);
    }

    @Override
    public List<Recipe> getDinnerRecommendations(Long userId, int limit) {
        return getRecommendedRecipes(userId, MealCategory.CENA, limit);
    }

    @Override
    public List<Recipe> getSnackRecommendations(Long userId, int limit) {
        return getRecommendedRecipes(userId, MealCategory.SNACK, limit);
    }

    /**
     * Árbol de Decisión para filtrar recetas
     */
    private List<Recipe> applyDecisionTree(User user, List<Recipe> recipes, MealCategory category) {
        System.out.println("🌳 APLICANDO ÁRBOL DE DECISIÓN");

        List<Recipe> filtered = recipes.stream()
                .filter(recipe -> {
                    boolean passes = filterByCategory(recipe, category);
                    if (!passes)
                        System.out.println("   ❌ Filtrado por categoría: " + recipe.getTitle());
                    return passes;
                })
                .filter(recipe -> {
                    boolean passes = filterByAllergies(user, recipe);
                    if (!passes)
                        System.out.println("   ❌ Filtrado por alergias: " + recipe.getTitle());
                    return passes;
                })
                .filter(recipe -> {
                    boolean passes = filterByDislikes(user, recipe);
                    if (!passes)
                        System.out.println("   ❌ Filtrado por disgustos: " + recipe.getTitle());
                    return passes;
                })
                .filter(recipe -> {
                    boolean passes = filterByWeightGoal(user, recipe);
                    if (!passes)
                        System.out.println("   ❌ Filtrado por meta de peso: " + recipe.getTitle() + " | Cal: "
                                + recipe.getCalories());
                    return passes;
                })
                .filter(recipe -> {
                    boolean passes = filterByNutritionalPreferences(user, recipe);
                    if (!passes)
                        System.out.println("   ❌ Filtrado por preferencias nutricionales: " + recipe.getTitle());
                    return passes;
                })
                .collect(Collectors.toList());

        System.out.println("🌳 Resultado árbol: " + filtered.size() + " recetas pasan los filtros");
        return filtered;
    }

    /**
     * Filtro 1: Categoría de comida
     */
    private boolean filterByCategory(Recipe recipe, MealCategory category) {
        return category == null || recipe.getCategory() == category;
    }

    /**
     * Filtro 2: Alergias del usuario - CORREGIDO
     */
    private boolean filterByAllergies(User user, Recipe recipe) {
        Set<String> userAllergies = user.getAllergies();
        if (userAllergies == null || userAllergies.isEmpty()) {
            return true;
        }

        // Convertir a lowercase para comparación case-insensitive
        Set<String> recipeIngredients = recipe.getIngredients().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> lowerAllergies = userAllergies.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        // Verificar si algún ingrediente contiene algún alérgeno
        boolean hasAllergy = recipeIngredients.stream()
                .anyMatch(ingredient -> lowerAllergies.stream()
                        .anyMatch(allergy -> ingredient.contains(allergy)));

        if (hasAllergy) {
            System.out.println("   ❌ Receta " + recipe.getTitle() + " contiene alergenos: " +
                    recipeIngredients.stream()
                            .filter(ingredient -> lowerAllergies.stream().anyMatch(ingredient::contains))
                            .collect(Collectors.toList()));
        }

        return !hasAllergy;
    }

    /**
     * Filtro 3: Ingredientes que disgustan al usuario - CORREGIDO
     */
    private boolean filterByDislikes(User user, Recipe recipe) {
        Set<String> userDislikes = user.getDislikedIngredients();
        if (userDislikes == null || userDislikes.isEmpty()) {
            return true;
        }

        Set<String> recipeIngredients = recipe.getIngredients().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Set<String> lowerDislikes = userDislikes.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        boolean hasDisliked = recipeIngredients.stream()
                .anyMatch(ingredient -> lowerDislikes.stream()
                        .anyMatch(dislike -> ingredient.contains(dislike)));

        if (hasDisliked) {
            System.out.println("   ❌ Receta " + recipe.getTitle() + " contiene ingredientes disgustados: " +
                    recipeIngredients.stream()
                            .filter(ingredient -> lowerDislikes.stream().anyMatch(ingredient::contains))
                            .collect(Collectors.toList()));
        }

        return !hasDisliked;
    }

    /**
     * Filtro 4: Metas de peso - CORREGIDO
     */
    /**
     * Filtro 4: Metas de peso - VERSIÓN FLEXIBLE
     */
    private boolean filterByWeightGoal(User user, Recipe recipe) {
        // NO filtrar por calorías - mostrar todas las recetas
        // El scoring se encargará de ordenarlas por relevancia
        return true;
    }

    /**
     * Filtro 5: Preferencias nutricionales - MEJORADO
     */
    private boolean filterByNutritionalPreferences(User user, Recipe recipe) {
        Set<Tag> userPreferences = user.getNutritionalPreferences();
        if (userPreferences == null || userPreferences.isEmpty()) {
            return true; // Si no tiene preferencias, no filtrar
        }

        List<Tag> recipeTags = recipe.getTags();
        if (recipeTags == null || recipeTags.isEmpty()) {
            return false; // Si la receta no tiene tags y el usuario sí tiene preferencias, filtrar
        }

        // Verificar si la receta tiene al menos una de las preferencias del usuario
        boolean matches = userPreferences.stream()
                .anyMatch(preference -> recipeTags.contains(preference));

        if (!matches) {
            System.out.println("   ❌ Receta " + recipe.getTitle() + " no coincide con preferencias: " +
                    userPreferences + " vs " + recipeTags);
        }

        return matches;
    }

    /**
     * Calcula el score de recomendación (0-100) - CORREGIDO
     */
    private double calculateRecommendationScore(User user, Recipe recipe) {
        double score = 50.0; // Score base

        // Bonus por coincidencia con preferencias nutricionales
        Set<Tag> userPreferences = user.getNutritionalPreferences();
        List<Tag> recipeTags = recipe.getTags();

        if (userPreferences != null && recipeTags != null) {
            long matchingPreferences = userPreferences.stream()
                    .filter(preference -> recipeTags.contains(preference))
                    .count();
            score += matchingPreferences * 10;
        }

        // Bonus por adecuación a meta de peso
        score += calculateWeightGoalBonus(user, recipe);

        // Bonus por balance nutricional
        score += calculateNutritionalBalanceBonus(recipe);

        // Bonus si es alta en proteína (generalmente deseable)
        if (hasHighProtein(recipe)) {
            score += 5;
        }

        // Bonus si es baja en calorías (para mayoría de metas)
        if (recipe.getCalories() < 400) {
            score += 3;
        }

        return Math.min(score, 100); // Máximo 100
    }

    private double calculateWeightGoalBonus(User user, Recipe recipe) {
        WeightGoal weightGoal = user.getWeightGoal();
        if (weightGoal == null)
            return 0;

        int calories = recipe.getCalories();

        switch (weightGoal) {
            case LOSE:
                // Máximo bonus para recetas bajas en calorías
                if (calories <= 350)
                    return 20;
                if (calories <= 450)
                    return 15;
                if (calories <= 550)
                    return 10;
                if (calories <= 650)
                    return 5;
                return 0;

            case MAINTAIN:
                // Bonus para recetas balanceadas
                if (calories >= 400 && calories <= 600)
                    return 15;
                if (calories >= 350 && calories <= 650)
                    return 10;
                if (calories >= 300 && calories <= 700)
                    return 5;
                return 0;

            case GAIN:
                // Máximo bonus para recetas altas en calorías y proteína
                if (calories >= 600 && hasHighProtein(recipe))
                    return 20;
                if (calories >= 500 && hasHighProtein(recipe))
                    return 15;
                if (calories >= 600)
                    return 10;
                if (calories >= 500)
                    return 5;
                return 0;

            default:
                return 0;
        }
    }

    private double calculateNutritionalBalanceBonus(Recipe recipe) {
        double bonus = 0;

        // Bonus por buen balance de macros
        if (hasGoodMacroBalance(recipe)) {
            bonus += 8;
        }

        // Bonus si tiene fibra (asumiendo que los tags lo indican)
        if (recipe.getTags() != null && recipe.getTags().contains(Tag.ALTA_FIBRA)) {
            bonus += 5;
        }

        return bonus;
    }

    private boolean hasHighProtein(Recipe recipe) {
        if (recipe.getCalories() == 0)
            return false;
        double proteinRatio = (double) recipe.getProtein() / recipe.getCalories();
        return proteinRatio > 0.3; // Más del 30% de calorías de proteína
    }

    private boolean hasGoodMacroBalance(Recipe recipe) {
        if (recipe.getCalories() == 0)
            return false;

        double proteinRatio = (double) recipe.getProtein() * 4 / recipe.getCalories();
        double carbRatio = (double) recipe.getCarbs() * 4 / recipe.getCalories();
        double fatRatio = (double) recipe.getFat() * 9 / recipe.getCalories();

        // Ratios balanceados: Proteína 20-30%, Carbos 40-50%, Grasa 25-35%
        return proteinRatio >= 0.2 && proteinRatio <= 0.35 &&
                carbRatio >= 0.4 && carbRatio <= 0.55 &&
                fatRatio >= 0.25 && fatRatio <= 0.35;
    }

    @Override
    public List<Recipe> getPersonalizedRecommendations(Long userId, int limit) {
        return getRecommendedRecipes(userId, null, limit);
    }
}