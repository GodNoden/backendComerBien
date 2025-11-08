package com.comerbien.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.comerbien.backend.model.entity.Favorite;
import com.comerbien.backend.model.entity.Recipe;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Encontrar todos los favoritos de un usuario
    List<Favorite> findByUserId(Long userId);

    // Encontrar si una receta es favorita de un usuario
    Optional<Favorite> findByUserIdAndRecipeId(Long userId, Long recipeId);

    // Verificar si una receta es favorita
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    // Contar favoritos de una receta
    Long countByRecipeId(Long recipeId);

    // Eliminar favorito por usuario y receta
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

    // Encontrar recetas favoritas de un usuario con paginación
    @Query("SELECT f.recipe FROM Favorite f WHERE f.user.id = :userId ORDER BY f.addedAt DESC")
    List<Recipe> findFavoriteRecipesByUserId(@Param("userId") Long userId);
}