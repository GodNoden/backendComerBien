package com.comerbien.backend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Tag;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class CreateRecipeRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String time;

    @NotNull
    private Difficulty difficulty;

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
    private Boolean isPublic = false;

    private MultipartFile imageFile;

    // Constructores, Getters y Setters
    public CreateRecipeRequest() {}

    // [Agregar todos los getters y setters]
}