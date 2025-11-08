package com.comerbien.backend.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.comerbien.backend.exception.CustomAuthenticationException;
import com.comerbien.backend.model.dto.request.LoginRequest;
import com.comerbien.backend.model.dto.request.RegisterRequest;
import com.comerbien.backend.model.dto.response.AuthResponse;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.repository.UserRepository;
import com.comerbien.backend.security.CustomUserDetails;
import com.comerbien.backend.security.JwtService;
import com.comerbien.backend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new CustomAuthenticationException("Username is already taken");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new CustomAuthenticationException("Email is already in use");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if (registerRequest.getDietaryPreferences() != null) {
            user.setDietaryPreferences(registerRequest.getDietaryPreferences());
        }

        if (registerRequest.getExcludedIngredients() != null) {
            user.setExcludedIngredients(registerRequest.getExcludedIngredients());
        }

        User savedUser = userRepository.save(user);

        // Generar token JWT
        String jwt = jwtService.generateToken(new CustomUserDetails(savedUser));

        return new AuthResponse(jwt, savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        User user = userDetails.getUser();
        return new AuthResponse(jwt, user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
