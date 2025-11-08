package com.comerbien.backend.model.dto.response;

import com.comerbien.backend.model.enums.DayOfWeek;
import com.comerbien.backend.model.enums.MealType;

public class DayMealResponse {

    private Long id;
    private DayOfWeek dayOfWeek;
    private MealType mealType;
    private RecipeResponse recipe;

    // Constructores
    public DayMealResponse() {
    }

    public DayMealResponse(DayOfWeek dayOfWeek, MealType mealType, RecipeResponse recipe) {
        this.dayOfWeek = dayOfWeek;
        this.mealType = mealType;
        this.recipe = recipe;
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

    public RecipeResponse getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeResponse recipe) {
        this.recipe = recipe;
    }
}
