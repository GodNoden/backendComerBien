package com.comerbien.backend.service.impl;

import org.springframework.stereotype.Service;

import com.comerbien.backend.exception.ResourceNotFoundException;
import com.comerbien.backend.model.dto.request.UserPreferencesRequest;
import com.comerbien.backend.model.dto.response.UserResponse;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.repository.UserRepository;
import com.comerbien.backend.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return convertToResponse(user);
    }

    @Override
    public UserResponse updateUserProfile(Long userId, UserResponse userResponse) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Actualizar campos permitidos
        if (userResponse.getEmail() != null && !userResponse.getEmail().equals(user.getEmail())) {
            // Verificar que el email no esté en uso
            if (userRepository.existsByEmail(userResponse.getEmail())) {
                throw new RuntimeException("Email is already in use");
            }
            user.setEmail(userResponse.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Override
    public UserResponse updateDietaryPreferences(Long userId, List<Tag> dietaryPreferences) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setDietaryPreferences(dietaryPreferences);
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Override
    public UserResponse updateExcludedIngredients(Long userId, List<String> excludedIngredients) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setExcludedIngredients(excludedIngredients);
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserPreferences(Long userId, UserPreferencesRequest preferences) {
        System.out.println("💾 GUARDANDO PREFERENCIAS PARA USER: " + userId);
        System.out.println("   - WeightGoal: " + preferences.getWeightGoal());
        System.out.println("   - ActivityLevel: " + preferences.getActivityLevel());
        System.out.println("   - RecommendedCalories: " + preferences.getRecommendedCalories());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Actualizar alergias - CORREGIDO (ya son Set)
        if (preferences.getAllergies() != null) {
            user.setAllergies(new HashSet<>(preferences.getAllergies()));
        }

        // Actualizar ingredientes que disgustan - CORREGIDO (ya son Set)
        if (preferences.getDislikedIngredients() != null) {
            user.setDislikedIngredients(new HashSet<>(preferences.getDislikedIngredients()));
        }

        // Actualizar meta de peso
        if (preferences.getWeightGoal() != null) {
            user.setWeightGoal(preferences.getWeightGoal());
        }

        // Actualizar preferencias nutricionales - CORREGIDO (ya son Set)
        if (preferences.getNutritionalPreferences() != null) {
            user.setNutritionalPreferences(new HashSet<>(preferences.getNutritionalPreferences()));
        }

        // Guardar información personal para recomendaciones
        if (preferences.getAge() != null) {
            user.setAge(preferences.getAge());
        }
        if (preferences.getHeight() != null) {
            user.setHeight(preferences.getHeight());
        }
        if (preferences.getWeight() != null) {
            user.setWeight(preferences.getWeight());
        }
        if (preferences.getGender() != null) {
            user.setGender(preferences.getGender());
        }
        if (preferences.getActivityLevel() != null) {
            user.setActivityLevel(preferences.getActivityLevel());
        }
        if (preferences.getRecommendedCalories() != null) {
            user.setRecommendedCalories(preferences.getRecommendedCalories());
        }

        User savedUser = userRepository.save(user);
        System.out.println("✅ PREFERENCIAS GUARDADAS: " + savedUser.getWeightGoal());

        return convertToResponse(savedUser);
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        // ✅ INCLUIR TODOS LOS CAMPOS NUEVOS
        response.setDietaryPreferences(
                user.getDietaryPreferences() != null ? new ArrayList<>(user.getDietaryPreferences())
                        : new ArrayList<>());

        response.setExcludedIngredients(
                user.getExcludedIngredients() != null ? new ArrayList<>(user.getExcludedIngredients())
                        : new ArrayList<>());

        // NUEVOS CAMPOS PARA RECOMENDACIONES
        response.setAllergies(user.getAllergies() != null ? new ArrayList<>(user.getAllergies()) : new ArrayList<>());

        response.setDislikedIngredients(
                user.getDislikedIngredients() != null ? new ArrayList<>(user.getDislikedIngredients())
                        : new ArrayList<>());

        response.setNutritionalPreferences(
                user.getNutritionalPreferences() != null ? new ArrayList<>(user.getNutritionalPreferences())
                        : new ArrayList<>());

        response.setWeightGoal(user.getWeightGoal());
        response.setAge(user.getAge());
        response.setHeight(user.getHeight());
        response.setWeight(user.getWeight());
        response.setGender(user.getGender());
        response.setActivityLevel(user.getActivityLevel());
        response.setRecommendedCalories(user.getRecommendedCalories());

        return response;
    }
}