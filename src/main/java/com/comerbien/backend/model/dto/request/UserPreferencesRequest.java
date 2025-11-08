package com.comerbien.backend.model.dto.request;

import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.model.enums.WeightGoal;

import java.util.List;

public class UserPreferencesRequest {
    private List<String> allergies;
    private List<String> dislikedIngredients;
    private WeightGoal weightGoal;
    private List<Tag> nutritionalPreferences;

    // Información personal para cálculo de calorías
    private Integer age;
    private Integer height; // en cm
    private Integer weight; // en kg
    private String gender;
    private String activityLevel;
    private Integer recommendedCalories;

    // Constructors
    public UserPreferencesRequest() {
    }

    // Getters and Setters
    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getDislikedIngredients() {
        return dislikedIngredients;
    }

    public void setDislikedIngredients(List<String> dislikedIngredients) {
        this.dislikedIngredients = dislikedIngredients;
    }

    public WeightGoal getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(WeightGoal weightGoal) {
        this.weightGoal = weightGoal;
    }

    public List<Tag> getNutritionalPreferences() {
        return nutritionalPreferences;
    }

    public void setNutritionalPreferences(List<Tag> nutritionalPreferences) {
        this.nutritionalPreferences = nutritionalPreferences;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Integer getRecommendedCalories() {
        return recommendedCalories;
    }

    public void setRecommendedCalories(Integer recommendedCalories) {
        this.recommendedCalories = recommendedCalories;
    }
}