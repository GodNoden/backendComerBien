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
        System.out.println("🌳 ===== INICIANDO ÁRBOL DE DECISIÓN =====");
        System.out.println("👤 Usuario ID: " + user.getId());
        System.out.println("📋 Preferencias actuales:");
        System.out.println("   - Alergias: " + user.getAllergies());
        System.out.println("   - Ingredientes disgustados: " + user.getDislikedIngredients());
        System.out.println("   - Preferencias nutricionales: " + user.getNutritionalPreferences());
        System.out.println("   - Meta de peso: " + user.getWeightGoal());
        System.out.println("🍽️  Recetas a evaluar: " + recipes.size());

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

    private boolean filterByAllergies(User user, Recipe recipe) {
        Set<String> userAllergies = user.getAllergies();
        if (userAllergies == null || userAllergies.isEmpty()) {
            return true;
        }

        // Convertir todo a lowercase
        Set<String> lowerAllergies = userAllergies.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<String> recipeIngredients = recipe.getIngredients().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        System.out.println("🔍 FILTRO ALERGIAS - Receta: " + recipe.getTitle());
        System.out.println("   Alergias: " + lowerAllergies);
        System.out.println("   Ingredientes: " + recipeIngredients);

        // Verificar cada alergia contra cada ingrediente
        for (String allergy : lowerAllergies) {
            for (String ingredient : recipeIngredients) {
                // Coincidencias más estrictas
                boolean matches =
                        // Coincidencia exacta
                        ingredient.equals(allergy) ||
                        // La alergia es una palabra completa en el ingrediente
                                Arrays.stream(ingredient.split("[,\\s]+"))
                                        .anyMatch(word -> word.equals(allergy))
                                ||
                                // El ingrediente contiene la alergia como substring (pero con espacios)
                                ingredient.contains(" " + allergy + " ") ||
                                ingredient.startsWith(allergy + " ") ||
                                ingredient.endsWith(" " + allergy);

                if (matches) {
                    System.out
                            .println("   ❌ ELIMINADA - Alergia '" + allergy + "' encontrada en: '" + ingredient + "'");
                    return false;
                }
            }
        }

        System.out.println("   ✅ PASÓ - No se encontraron alergias en: " + recipe.getTitle());
        return true;
    }

    /**
     * Filtro 3: Ingredientes que disgustan al usuario - CORREGIDO
     */
    /**
     * Filtro 3: Ingredientes que disgustan al usuario - MEJORADO
     */
    private boolean filterByDislikes(User user, Recipe recipe) {
        Set<String> userDislikes = user.getDislikedIngredients();
        if (userDislikes == null || userDislikes.isEmpty()) {
            return true;
        }

        Set<String> lowerDislikes = userDislikes.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<String> recipeIngredients = recipe.getIngredients().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        System.out.println("🔍 FILTRO DISGUSTOS - Receta: " + recipe.getTitle());
        System.out.println("   Disgustos: " + lowerDislikes);
        System.out.println("   Ingredientes: " + recipeIngredients);

        for (String dislike : lowerDislikes) {
            for (String ingredient : recipeIngredients) {
                boolean matches = ingredient.equals(dislike) ||
                        Arrays.stream(ingredient.split("[,\\s]+"))
                                .anyMatch(word -> word.equals(dislike))
                        ||
                        ingredient.contains(" " + dislike + " ") ||
                        ingredient.startsWith(dislike + " ") ||
                        ingredient.endsWith(" " + dislike);

                if (matches) {
                    System.out.println("   ❌ ELIMINADA - Ingrediente disgustado '" + dislike + "' encontrado en: '"
                            + ingredient + "'");
                    return false;
                }
            }
        }

        System.out.println("   ✅ PASÓ - No se encontraron ingredientes disgustados en: " + recipe.getTitle());
        return true;
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
        double proteinRatio = recipe.getCalories() > 0 ? (double) recipe.getProtein() * 4 / recipe.getCalories() : 0;

        // AJUSTADO para rango 93-296 calorías
        switch (weightGoal) {
            case LOSE:
                // Priorizar recetas BAJAS en calorías dentro del rango disponible
                if (calories <= 120 && proteinRatio > 0.25)
                    return 40;
                if (calories <= 150)
                    return 30;
                if (calories <= 180)
                    return 20;
                if (calories <= 200)
                    return 10;
                if (calories <= 220)
                    return 5;
                return -10; // Penalizar las más altas (> 220)

            case MAINTAIN:
                // Rango medio-balanceado
                if (calories >= 140 && calories <= 220 && proteinRatio > 0.2)
                    return 35;
                if (calories >= 120 && calories <= 240)
                    return 25;
                if (calories >= 100 && calories <= 260)
                    return 15;
                return 0;

            case GAIN:
                // Priorizar las MÁS ALTAS disponibles y con buena proteína
                if (calories >= 220 && proteinRatio > 0.3)
                    return 45;
                if (calories >= 200 && proteinRatio > 0.25)
                    return 35;
                if (calories >= 180)
                    return 25;
                if (calories >= 160)
                    return 15;
                if (calories >= 140)
                    return 5;
                return -5; // Penalizar las más bajas (< 140)

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