package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.comerbien.backend.model.dto.response.WeeklyMenuResponse;
import com.comerbien.backend.model.dto.response.DayMealsResponse;
import com.comerbien.backend.model.enums.DayOfWeek;
import com.comerbien.backend.model.enums.MealType;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.service.WeeklyMenuService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "http://localhost:8080")
public class WeeklyMenuController {

    private final WeeklyMenuService weeklyMenuService;

    public WeeklyMenuController(WeeklyMenuService weeklyMenuService) {
        this.weeklyMenuService = weeklyMenuService;
    }

    @GetMapping("/current")
    public ResponseEntity<WeeklyMenuResponse> getCurrentWeekMenu(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🎯 Getting current week menu for user: " + userDetails.getUser().getId());
            WeeklyMenuResponse menu = weeklyMenuService.getCurrentWeekMenu(userDetails.getUser().getId());
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            System.out.println("❌ Error getting current week menu: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/week/{weekStartDate}")
    public ResponseEntity<WeeklyMenuResponse> getOrCreateWeeklyMenu(
            @PathVariable LocalDate weekStartDate,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println(
                    "🎯 Getting/Creating menu for week: " + weekStartDate + ", user: " + userDetails.getUser().getId());
            WeeklyMenuResponse menu = weeklyMenuService.getOrCreateWeeklyMenu(userDetails.getUser().getId(),
                    weekStartDate);
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            System.out.println("❌ Error getting/creating weekly menu: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<WeeklyMenuResponse> getWeeklyMenu(
            @PathVariable Long menuId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🎯 Getting menu: " + menuId + " for user: " + userDetails.getUser().getId());
            WeeklyMenuResponse menu = weeklyMenuService.getWeeklyMenu(menuId, userDetails.getUser().getId());
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            System.out.println("❌ Error getting weekly menu: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<WeeklyMenuResponse>> getUserWeeklyMenus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🎯 Getting all menus for user: " + userDetails.getUser().getId());
            List<WeeklyMenuResponse> menus = weeklyMenuService.getUserWeeklyMenus(userDetails.getUser().getId());
            return ResponseEntity.ok(menus);
        } catch (Exception e) {
            System.out.println("❌ Error getting user menus: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{menuId}/days/{dayOfWeek}/meals/{mealType}/recipes/{recipeId}")
    public ResponseEntity<WeeklyMenuResponse> addRecipeToMenu(
            @PathVariable Long menuId,
            @PathVariable DayOfWeek dayOfWeek,
            @PathVariable MealType mealType,
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🎯 Adding recipe to menu - Menu: " + menuId + ", Day: " + dayOfWeek +
                    ", Meal: " + mealType + ", Recipe: " + recipeId);
            WeeklyMenuResponse menu = weeklyMenuService.addRecipeToMenu(menuId, dayOfWeek, mealType, recipeId,
                    userDetails.getUser().getId());
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            System.out.println("❌ Error adding recipe to menu: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{menuId}/days/{dayOfWeek}/meals/{mealType}")
    public ResponseEntity<WeeklyMenuResponse> removeRecipeFromMenu(
            @PathVariable Long menuId,
            @PathVariable DayOfWeek dayOfWeek,
            @PathVariable MealType mealType,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println(
                    "🎯 Removing recipe from menu - Menu: " + menuId + ", Day: " + dayOfWeek + ", Meal: " + mealType);
            WeeklyMenuResponse menu = weeklyMenuService.removeRecipeFromMenu(menuId, dayOfWeek, mealType,
                    userDetails.getUser().getId());
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            System.out.println("❌ Error removing recipe from menu: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{menuId}/days/{dayOfWeek}")
    public ResponseEntity<DayMealsResponse> getDayMeals(
            @PathVariable Long menuId,
            @PathVariable DayOfWeek dayOfWeek,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🎯 Getting day meals - Menu: " + menuId + ", Day: " + dayOfWeek);
            DayMealsResponse dayMeals = weeklyMenuService.getDayMeals(menuId, dayOfWeek, userDetails.getUser().getId());
            return ResponseEntity.ok(dayMeals);
        } catch (Exception e) {
            System.out.println("❌ Error getting day meals: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteWeeklyMenu(
            @PathVariable Long menuId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            System.out.println("🎯 Deleting menu: " + menuId);
            weeklyMenuService.deleteWeeklyMenu(menuId, userDetails.getUser().getId());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println("❌ Error deleting weekly menu: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}