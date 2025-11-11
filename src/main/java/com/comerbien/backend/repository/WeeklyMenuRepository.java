package com.comerbien.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.comerbien.backend.model.entity.WeeklyMenu;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeeklyMenuRepository extends JpaRepository<WeeklyMenu, Long> {

    // Encontrar menús por usuario
    List<WeeklyMenu> findByUserId(Long userId);

    // Encontrar menú por usuario y fecha de inicio
    Optional<WeeklyMenu> findByUserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);

    // Encontrar el menú más reciente de un usuario
    @Query("SELECT wm FROM WeeklyMenu wm WHERE wm.user.id = :userId ORDER BY wm.weekStartDate DESC LIMIT 1")
    Optional<WeeklyMenu> findMostRecentByUserId(@Param("userId") Long userId);

    // Verificar si existe menú para un usuario en una semana específica
    Boolean existsByUserIdAndWeekStartDate(Long userId, LocalDate weekStartDate);

    @Query("SELECT wm FROM WeeklyMenu wm LEFT JOIN FETCH wm.dayMeals dm LEFT JOIN FETCH dm.recipe WHERE wm.id = :id")
    Optional<WeeklyMenu> findByIdWithDayMeals(@Param("id") Long id);
}
