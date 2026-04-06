package com.eadevs.finance_app.model.auth;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String message;
    private String username;

    public LoginResponse(String token, String message, String username) {
        this.token = token;
        this.message = message;
        this.username = username;
    }
}
