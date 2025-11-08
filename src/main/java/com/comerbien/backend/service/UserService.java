package com.comerbien.backend.service;

import com.comerbien.backend.model.dto.request.UserPreferencesRequest;
import com.comerbien.backend.model.dto.response.UserResponse;
import com.comerbien.backend.model.enums.Tag;
import java.util.List;

public interface UserService {
    UserResponse getUserProfile(Long userId);

    UserResponse updateUserProfile(Long userId, UserResponse userResponse);

    UserResponse updateDietaryPreferences(Long userId, List<Tag> dietaryPreferences);

    UserResponse updateExcludedIngredients(Long userId, List<String> excludedIngredients);

    UserResponse updateUserPreferences(Long userId, UserPreferencesRequest preferences);
}