package com.comerbien.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Tag;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RecipeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String time;

    @NotNull
    private Difficulty difficulty;

    private String image; // Para URLs existentes o base64

    private MultipartFile imageFile; // Nuevo campo para upload de archivos

    @NotNull
    private MealCategory category;

    @NotNull
    @Positive
    private Integer calories;

    @NotNull
    @Positive
    private Integer protein;

    @NotNull
    @Positive
    private Integer carbs;

    @NotNull
    @Positive
    private Integer fat;

    private List<Tag> tags;
    private List<String> ingredients;
    private String instructions;
    private Boolean isPublic = false; // Por defecto recetas privadas

    // Constructores
    public RecipeRequest() {
    }

    // Getters y Setters (agregar los nuevos)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public MealCategory getCategory() {
        return category;
    }

    public void setCategory(MealCategory category) {
        this.category = category;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
}