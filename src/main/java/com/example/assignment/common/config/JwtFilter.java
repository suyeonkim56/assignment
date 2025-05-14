package com.example.assignment.common.config;

import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Swagger 관련 요청은 필터 제외
        if (path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars/")
                || path.startsWith("/configuration/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = jwtUtil.substringToken(authHeader);

            try {
                var claims = jwtUtil.extractClaims(token);
                Long userId = Long.valueOf(claims.getSubject());
                String username = claims.get("username", String.class);

                request.setAttribute("userId", userId);
                request.setAttribute("username", username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException | MalformedJwtException e) {
                throw new ClientException(ErrorCode.INVALID_JWT_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

}
