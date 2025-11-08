package com.comerbien.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.model.enums.WeightGoal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(nullable = false)
    private String password;

    @ElementCollection
    @CollectionTable(name = "user_dietary_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "preference")
    private List<Tag> dietaryPreferences = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_excluded_ingredients", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ingredient")
    private List<String> excludedIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyMenu> weeklyMenus = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_allergies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "allergy")
    private Set<String> allergies = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_disliked_ingredients", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ingredient")
    private Set<String> dislikedIngredients = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_goal")
    private WeightGoal weightGoal;

    @ElementCollection
    @CollectionTable(name = "user_nutritional_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "preference")
    private Set<Tag> nutritionalPreferences = new HashSet<>();

    // Campos para cálculo de calorías
    private Integer age;
    private Integer height; // cm
    private Integer weight; // kg
    private String gender;
    private String activityLevel;
    private Integer recommendedCalories;

    // Constructores
    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<WeeklyMenu> getWeeklyMenus() {
        return weeklyMenus;
    }

    public void setWeeklyMenus(List<WeeklyMenu> weeklyMenus) {
        this.weeklyMenus = weeklyMenus;
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

    public Set<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(Set<String> allergies) {
        this.allergies = allergies;
    }

    public Set<String> getDislikedIngredients() {
        return dislikedIngredients;
    }

    public void setDislikedIngredients(Set<String> dislikedIngredients) {
        this.dislikedIngredients = dislikedIngredients;
    }

    public WeightGoal getWeightGoal() {
        return weightGoal;
    }

    public void setWeightGoal(WeightGoal weightGoal) {
        this.weightGoal = weightGoal;
    }

    public Set<Tag> getNutritionalPreferences() {
        return nutritionalPreferences;
    }

    public void setNutritionalPreferences(Set<Tag> nutritionalPreferences) {
        this.nutritionalPreferences = nutritionalPreferences;
    }
}