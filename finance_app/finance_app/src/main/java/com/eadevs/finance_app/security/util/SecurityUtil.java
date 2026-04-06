package com.eadevs.finance_app.security.util;

import com.eadevs.finance_app.model.User;
import com.eadevs.finance_app.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getCurrentUserEmail() {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new RuntimeException("No authenticated user found");
        }

        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public static User getCurrentUser(UserRepository userRepository) {

        String email = getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}