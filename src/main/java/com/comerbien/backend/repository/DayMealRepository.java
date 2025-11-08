package com.comerbien.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.comerbien.backend.model.entity.DayMeal;
import com.comerbien.backend.model.enums.DayOfWeek;
import com.comerbien.backend.model.enums.MealType;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayMealRepository extends JpaRepository<DayMeal, Long> {

        // Encontrar comidas por menú semanal
        List<DayMeal> findByWeeklyMenuId(Long weeklyMenuId);

        // Encontrar comida específica por menú, día y tipo de comida
        Optional<DayMeal> findByWeeklyMenuIdAndDayOfWeekAndMealType(
                        Long weeklyMenuId, DayOfWeek dayOfWeek, MealType mealType);

        // Encontrar todas las comidas de un día específico en un menú
        List<DayMeal> findByWeeklyMenuIdAndDayOfWeek(Long weeklyMenuId, DayOfWeek dayOfWeek);

        // Encontrar todas las comidas de un tipo específico en un menú
        List<DayMeal> findByWeeklyMenuIdAndMealType(Long weeklyMenuId, MealType mealType);

        // Verificar si existe una comida en un slot específico
        Boolean existsByWeeklyMenuIdAndDayOfWeekAndMealType(
                        Long weeklyMenuId, DayOfWeek dayOfWeek, MealType mealType);

        // Eliminar todas las comidas de un menú semanal
        void deleteByWeeklyMenuId(Long weeklyMenuId);
}