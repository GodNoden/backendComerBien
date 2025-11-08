package com.comerbien.backend.model.dto.response;

import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.model.enums.WeightGoal;
import java.util.List;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private List<Tag> dietaryPreferences;
    private List<String> excludedIngredients;

    // ✅ NUEVOS CAMPOS PARA RECOMENDACIONES
    private List<String> allergies;
    private List<String> dislikedIngredients;
    private List<Tag> nutritionalPreferences;
    private WeightGoal weightGoal;

    // Campos para cálculo de calorías
    private Integer age;
    private Integer height;
    private Integer weight;
    private String gender;
    private String activityLevel;
    private Integer recommendedCalories;

    // Constructores
    public UserResponse() {
    }

    public UserResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Tag> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(List<Tag> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }

    public List<String> getExcludedIngredients() {
        return excludedIngredients;
    }

    public void setExcludedIngredients(List<String> excludedIngredients) {
        this.excludedIngredients = excludedIngredients;
    }

    // ✅ NUEVOS GETTERS Y SETTERS
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

    public List<Tag> getNutritionalPreferences() {
        return nutritionalPreferences;
    }

    public void setNutritionalPreferences(List<Tag> nutritionalPreferences) {
        this.nutritionalPreferences = nutritionalPreferences;
    }

    public WeightGoal getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(WeightGoal weightGoal) {
        this.weightGoal = weightGoal;
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