package com.eadevs.finance_app.service;

import com.eadevs.finance_app.dto.UserRequestDTO;
import com.eadevs.finance_app.model.auth.LoginResponse;
import com.eadevs.finance_app.repository.UserRepository;
import com.eadevs.finance_app.security.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JwtService {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public JwtService(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(String username, String password) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtil.generateToken(username);
        return new LoginResponse(jwtToken, "Login Successful", username);
    }

}
