package com.project.insurtech.components.helpers;

import com.project.insurtech.components.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class RequestHelper {
    private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);
    private final JwtTokenUtil jwtTokenUtil;

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public Long getUserId(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            logger.warn("Authorization token is missing or invalid.");
            return null; // Or you could throw an exception, depending on your design
        }

        try {
            Long userId = jwtTokenUtil.extractClaim(token, claims -> claims.get("userId", Long.class));
            if (userId == null) {
                logger.warn("User ID not found in token claims.");
            }
            return userId;
        } catch (Exception e) {
            logger.error("Error extracting user ID from token: {}", e.getMessage());
            return null; // Handle token extraction errors
        }
    }

}
