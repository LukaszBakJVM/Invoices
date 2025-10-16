package org.lukasz.faktury.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String message;

        if (exception instanceof InternalAuthenticationServiceException && exception.getCause() != null) {
            message = exception.getCause().getMessage();

        } else if (exception.getMessage() != null && !exception.getMessage().contains("Bad credentials")) {
            message = exception.getMessage();
        } else {
            message = exception.getMessage();
        }

        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, "/login?error=" + encodedMessage);
    }
}

