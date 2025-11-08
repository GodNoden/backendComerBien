package com.comerbien.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.comerbien.backend.model.enums.DayOfWeek;
import com.comerbien.backend.model.enums.MealType;

@Entity
@Table(name = "day_meals", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "weekly_menu_id", "day_of_week", "meal_type" })
})
public class DayMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_menu_id", nullable = false)
    private WeeklyMenu weeklyMenu;

    // Constructores
    public DayMeal() {
    }

    public DayMeal(DayOfWeek dayOfWeek, MealType mealType, Recipe recipe, WeeklyMenu weeklyMenu) {
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
        this.recipe = recipe;
        this.weeklyMenu = weeklyMenu;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public WeeklyMenu getWeeklyMenu() {
        return weeklyMenu;
    }

    public void setWeeklyMenu(WeeklyMenu weeklyMenu) {
        this.weeklyMenu = weeklyMenu;
    }
}
