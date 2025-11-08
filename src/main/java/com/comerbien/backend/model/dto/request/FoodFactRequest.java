package com.comerbien.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import com.comerbien.backend.model.enums.MealCategory;
import java.util.List;

public class FoodFactRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String fact;

    @NotBlank
    private String source;

    private MealCategory category;
    private List<String> keywords;
    private List<Long> relatedRecipeIds;
    private Boolean isActive = true;
    private String sourceUrl; // ✅ NUEVO

    // Constructores
    public FoodFactRequest() {
    }

    // Getters y Setters
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

    public List<Long> getRelatedRecipeIds() {
        return relatedRecipeIds;
    }

    public void setRelatedRecipeIds(List<Long> relatedRecipeIds) {
        this.relatedRecipeIds = relatedRecipeIds;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // Agregar getter y setter
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}