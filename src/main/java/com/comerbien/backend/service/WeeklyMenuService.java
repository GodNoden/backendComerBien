package com.comerbien.backend.service;

import com.comerbien.backend.model.dto.response.WeeklyMenuResponse;
import com.comerbien.backend.model.dto.response.DayMealsResponse;
import com.comerbien.backend.model.enums.DayOfWeek;
import com.comerbien.backend.model.enums.MealType;
import java.time.LocalDate;
import java.util.List;

public interface WeeklyMenuService {
    WeeklyMenuResponse getOrCreateWeeklyMenu(Long userId, LocalDate weekStartDate);
    WeeklyMenuResponse getWeeklyMenu(Long menuId, Long userId);
    List<WeeklyMenuResponse> getUserWeeklyMenus(Long userId);
    WeeklyMenuResponse addRecipeToMenu(Long menuId, DayOfWeek dayOfWeek, MealType mealType, Long recipeId, Long userId);
    WeeklyMenuResponse removeRecipeFromMenu(Long menuId, DayOfWeek dayOfWeek, MealType mealType, Long userId);
    void deleteWeeklyMenu(Long menuId, Long userId);
    DayMealsResponse getDayMeals(Long menuId, DayOfWeek dayOfWeek, Long userId);
    WeeklyMenuResponse getCurrentWeekMenu(Long userId);
}
