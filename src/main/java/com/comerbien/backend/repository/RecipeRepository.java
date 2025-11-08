package com.comerbien.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.Tag;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Encontrar recetas por categoría
    List<Recipe> findByCategory(MealCategory category);

    // Encontrar recetas por dificultad
    List<Recipe> findByDifficulty(Difficulty difficulty);

    // Encontrar recetas públicas
    List<Recipe> findByIsPublicTrue();

    // Encontrar recetas por usuario creador
    List<Recipe> findByCreatedById(Long userId);

    // Encontrar recetas públicas y del usuario específico
    List<Recipe> findByIsPublicTrueOrCreatedById(Long userId);

    // Encontrar recetas que contengan cierto tag
    @Query("SELECT r FROM Recipe r JOIN r.tags t WHERE t = :tag")
    List<Recipe> findByTag(@Param("tag") Tag tag);

    // Encontrar recetas por título (búsqueda)
    List<Recipe> findByTitleContainingIgnoreCase(String title);

    // Encontrar recetas que no contengan ingredientes excluidos
    @Query("SELECT r FROM Recipe r WHERE NOT EXISTS (" +
            "SELECT 1 FROM r.ingredients i WHERE i IN :excludedIngredients)")
    List<Recipe> findByIngredientsNotIn(@Param("excludedIngredients") List<String> excludedIngredients);
}
