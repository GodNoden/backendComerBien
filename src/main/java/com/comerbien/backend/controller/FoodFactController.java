package com.comerbien.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.comerbien.backend.model.dto.request.FoodFactRequest;
import com.comerbien.backend.model.dto.response.FoodFactResponse;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.service.FoodFactService;
import java.util.List;

@RestController
@RequestMapping("/api/food-facts")
@CrossOrigin(origins = "http://localhost:8080")
public class FoodFactController {

       private final FoodFactService foodFactService;

       public FoodFactController(FoodFactService foodFactService) {
              this.foodFactService = foodFactService;
       }

       @GetMapping
       public ResponseEntity<List<FoodFactResponse>> getAllFoodFacts() {
              List<FoodFactResponse> foodFacts = foodFactService.getAllFoodFacts();
              return ResponseEntity.ok(foodFacts);
       }

       @GetMapping("/active")
       public ResponseEntity<List<FoodFactResponse>> getActiveFoodFacts() {
              List<FoodFactResponse> foodFacts = foodFactService.getActiveFoodFacts();
              return ResponseEntity.ok(foodFacts);
       }

       @GetMapping("/{id}")
       public ResponseEntity<FoodFactResponse> getFoodFactById(@PathVariable Long id) {
              FoodFactResponse foodFact = foodFactService.getFoodFactById(id);
              return ResponseEntity.ok(foodFact);
       }

       @PostMapping
       public ResponseEntity<FoodFactResponse> createFoodFact(@RequestBody FoodFactRequest foodFactRequest) {
              FoodFactResponse foodFact = foodFactService.createFoodFact(foodFactRequest);
              return ResponseEntity.ok(foodFact);
       }

       @PutMapping("/{id}")
       public ResponseEntity<FoodFactResponse> updateFoodFact(
                     @PathVariable Long id,
                     @RequestBody FoodFactRequest foodFactRequest) {
              FoodFactResponse foodFact = foodFactService.updateFoodFact(id, foodFactRequest);
              return ResponseEntity.ok(foodFact);
       }

       @DeleteMapping("/{id}")
       public ResponseEntity<Void> deleteFoodFact(@PathVariable Long id) {
              foodFactService.deleteFoodFact(id);
              return ResponseEntity.noContent().build();
       }

       @GetMapping("/category/{category}")
       public ResponseEntity<List<FoodFactResponse>> getFoodFactsByCategory(@PathVariable MealCategory category) {
              List<FoodFactResponse> foodFacts = foodFactService.getFoodFactsByCategory(category);
              return ResponseEntity.ok(foodFacts);
       }

       @GetMapping("/search")
       public ResponseEntity<List<FoodFactResponse>> searchFoodFacts(@RequestParam String q) {
              List<FoodFactResponse> foodFacts = foodFactService.searchFoodFacts(q);
              return ResponseEntity.ok(foodFacts);
       }

       @GetMapping("/recipe/{recipeId}")
       public ResponseEntity<List<FoodFactResponse>> getRelevantFactsForRecipe(@PathVariable Long recipeId) {
              List<FoodFactResponse> foodFacts = foodFactService.getRelevantFactsForRecipe(recipeId);
              return ResponseEntity.ok(foodFacts);
       }

       @GetMapping("/category/{category}/relevant")
       public ResponseEntity<List<FoodFactResponse>> getRelevantFactsForCategory(@PathVariable MealCategory category) {
              List<FoodFactResponse> foodFacts = foodFactService.getRelevantFactsForCategory(category);
              return ResponseEntity.ok(foodFacts);
       }

       @PostMapping("/match-recipes")
       public ResponseEntity<Void> matchFoodFactsWithRecipes() {
              foodFactService.matchFoodFactsWithRecipes();
              return ResponseEntity.ok().build();
       }

       @GetMapping("/test")
       public ResponseEntity<String> testEndpoint() {
              return ResponseEntity.ok("✅ FoodFacts API is working!");
       }
}