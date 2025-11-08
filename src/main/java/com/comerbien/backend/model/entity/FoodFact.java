package com.comerbien.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import com.comerbien.backend.model.enums.MealCategory;

@Entity
@Table(name = "food_facts")
public class FoodFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String fact;

    @NotBlank
    @Column(nullable = false)
    private String source;

    @Enumerated(EnumType.STRING)
    private MealCategory category;

    @ElementCollection
    @CollectionTable(name = "food_fact_keywords", joinColumns = @JoinColumn(name = "food_fact_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "food_fact_related_recipes", joinColumns = @JoinColumn(name = "food_fact_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private List<Recipe> relatedRecipes = new ArrayList<>();

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    private Boolean isActive = true;

    // Constructores
    public FoodFact() {
    }

    public FoodFact(String title, String fact, String source, MealCategory category, String sourceUrl) {
        this.title = title;
        this.fact = fact;
        this.source = source;
        this.category = category;
        this.sourceUrl = sourceUrl;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MealCategory getCategory() {
        return category;
    }

    public void setCategory(MealCategory category) {
        this.category = category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<Recipe> getRelatedRecipes() {
        return relatedRecipes;
    }

    public void setRelatedRecipes(List<Recipe> relatedRecipes) {
        this.relatedRecipes = relatedRecipes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}