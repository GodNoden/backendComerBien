package com.comerbien.backend.service;

import com.comerbien.backend.model.dto.request.LoginRequest;
import com.comerbien.backend.model.dto.request.RegisterRequest;
import com.comerbien.backend.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}