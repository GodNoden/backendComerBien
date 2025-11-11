package com.comerbien.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.comerbien.backend.model.entity.FoodFact;
import com.comerbien.backend.model.enums.MealCategory;
import java.util.List;
import java.util.Optional;

public interface FoodFactRepository extends JpaRepository<FoodFact, Long> {

        // Encontrar facts por categoría
        List<FoodFact> findByCategory(MealCategory category);

        // Encontrar facts activos
        List<FoodFact> findByIsActiveTrue();

        // Encontrar facts por categoría y activos
        List<FoodFact> findByCategoryAndIsActiveTrue(MealCategory category);

        // Buscar facts por keywords
        @Query("SELECT f FROM FoodFact f WHERE f.isActive = true AND " +
                        "EXISTS (SELECT k FROM f.keywords k WHERE LOWER(k) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        List<FoodFact> findByKeyword(@Param("keyword") String keyword);

        // Encontrar facts relacionados con una receta específica por ingredientes
        @Query("SELECT DISTINCT f FROM FoodFact f WHERE f.isActive = true AND " +
                        "EXISTS (SELECT k FROM f.keywords k WHERE " +
                        "EXISTS (SELECT i FROM Recipe r JOIN r.ingredients i WHERE r.id = :recipeId AND LOWER(i) LIKE LOWER(CONCAT('%', k, '%'))))")
        List<FoodFact> findRelevantFactsForRecipe(@Param("recipeId") Long recipeId);

        // Encontrar facts por categoría de receta
        // @Query("SELECT f FROM FoodFact f WHERE f.isActive = true AND " +
        // "(f.category = :category OR f.category IS NULL)")
        // List<FoodFact> findRelevantFactsForCategory(@Param("category") MealCategory
        // category);
        @Query("SELECT f FROM FoodFact f WHERE f.isActive = true AND " +
                        "f.category = :category " +
                        "ORDER BY f.id DESC") // ← ORDENAR por ID descendente (más nuevos primero)
        List<FoodFact> findRelevantFactsForCategory(@Param("category") MealCategory category);

        @Query("SELECT f FROM FoodFact f WHERE f.isActive = true " +
                        "ORDER BY f.id DESC LIMIT :limit")
        List<FoodFact> findRecentFacts(@Param("limit") int limit);

        @Query(value = "SELECT * FROM food_facts f WHERE f.is_active = true ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
        List<FoodFact> findRandomFacts(@Param("limit") int limit);
}