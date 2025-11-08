package com.comerbien.backend.model.dto.response;

import java.util.List;

public class DayMealsResponse {
    
    private List<RecipeResponse> desayuno;
    private List<RecipeResponse> comida;
    private List<RecipeResponse> cena;
    private List<RecipeResponse> snack;

    // Constructores
    public DayMealsResponse() {}

    // Getters y Setters
    public List<RecipeResponse> getDesayuno() { return desayuno; }
    public void setDesayuno(List<RecipeResponse> desayuno) { this.desayuno = desayuno; }

    public List<RecipeResponse> getComida() { return comida; }
    public void setComida(List<RecipeResponse> comida) { this.comida = comida; }

    public List<RecipeResponse> getCena() { return cena; }
    public void setCena(List<RecipeResponse> cena) { this.cena = cena; }

    public List<RecipeResponse> getSnack() { return snack; }
    public void setSnack(List<RecipeResponse> snack) { this.snack = snack; }
}
