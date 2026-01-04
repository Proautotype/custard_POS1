package com.pos.base.config;

// Save this as a new file: RedirectAuthenticationSuccessHandler.java
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RedirectAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String targetUrl = "/"; // The path to redirect to after successful login

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Clear out any old session attributes or redirect parameters
        // and force a clean HTTP redirect to the main view.
        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}