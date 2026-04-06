package com.eadevs.finance_app.controller;

import com.eadevs.finance_app.model.auth.LoginRequest;
import com.eadevs.finance_app.model.auth.LoginResponse;
import com.eadevs.finance_app.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        LoginResponse response =
                jwtService.login(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(response);
    }
}
