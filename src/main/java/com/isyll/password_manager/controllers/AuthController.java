package com.isyll.password_manager.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isyll.password_manager.config.i18n.I18nUtil;
import com.isyll.password_manager.dto.mapper.UserMapper;
import com.isyll.password_manager.dto.payload.request.SignUpRequest;
import com.isyll.password_manager.dto.payload.request.SigninRequest;
import com.isyll.password_manager.dto.payload.response.ApiResponse;
import com.isyll.password_manager.dto.payload.response.JwtResponse;
import com.isyll.password_manager.models.User;
import com.isyll.password_manager.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "API to manage authentication.")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper mapper;

    @Autowired
    I18nUtil i18nUtil;

    @PostMapping("signin")
    public ResponseEntity<ApiResponse<JwtResponse>> signin(@RequestBody @Valid SigninRequest signinRequest) {
        String email = signinRequest.getEmail();
        String password = signinRequest.getPassword();

        String accessToken = userService.generateAccessToken(email, password);
        String refreshToken = userService.generateRefreshToken(email, password);
        JwtResponse response = new JwtResponse(accessToken, refreshToken);

        return ApiResponse.success(response).toReponseEntity();
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse<User>> signup(@RequestBody @Valid SignUpRequest signupRequest) {
        User user = new User();
        mapper.updateUserFromSignupRequest(signupRequest, user);
        User createdUser = userService.createUser(user);

        String message = i18nUtil.getMessage("auth.user_registered_successfully");

        return ApiResponse.success(createdUser, message, HttpStatus.CREATED).toReponseEntity();
    }

    @PostMapping("refresh-token")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        String acessToken = userService.generateFromRefreshToken(refreshToken);

        JwtResponse response = new JwtResponse(acessToken, refreshToken);

        return ApiResponse.success(response).toReponseEntity();
    }
}
