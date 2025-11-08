package com.comerbien.backend.model.dto.response;

import com.comerbien.backend.model.enums.MealCategory;
import java.util.List;

public class FoodFactResponse {

    private Long id;
    private String title;
    private String fact;
    private String source;
    private MealCategory category;
    private List<String> keywords;
    private List<RecipeResponse> relatedRecipes;
    private Boolean isActive;
    private Integer relatedRecipesCount;
    private String sourceUrl; // ✅ NUEVO

    // Constructores
    public FoodFactResponse() {
    }

    public FoodFactResponse(Long id, String title, String fact, String source, MealCategory category,
            String sourceUrl) {
        this.id = id;
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

    public List<RecipeResponse> getRelatedRecipes() {
        return relatedRecipes;
    }

    public void setRelatedRecipes(List<RecipeResponse> relatedRecipes) {
        this.relatedRecipes = relatedRecipes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getRelatedRecipesCount() {
        return relatedRecipesCount;
    }

    public void setRelatedRecipesCount(Integer relatedRecipesCount) {
        this.relatedRecipesCount = relatedRecipesCount;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}