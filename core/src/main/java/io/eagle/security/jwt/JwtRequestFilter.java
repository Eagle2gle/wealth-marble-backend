package io.eagle.security.jwt;

import io.eagle.auth.AuthDetails;
import io.eagle.auth.AuthService;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    // 인증에서 제외할 URL
    private static final List<String> EXCLUDE_URL = Collections.unmodifiableList(
        Arrays.asList(
            "/admin"
        )
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        String jwtToken = getJwtToken(token);
        String providerId = getProviderIdFromToken(jwtToken);
        Long userId = getUserIdFromToken(jwtToken);

        if (providerId != null) {
            if (validateAuthentication(jwtToken)) {
                assignAuthenticationUser(userId);
            }
            generateRefreshToken(providerId);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private String getProviderIdFromToken(String jwtToken) {
        if (jwtToken != null) {
            return jwtTokenProvider.getUsernameFromToken(jwtToken);
        }
        return null;
    }

    private Long getUserIdFromToken(String jwtToken) {
        if (jwtToken != null) {
            return jwtTokenProvider.getUserIdFromToken(jwtToken);
        }
        return null;
    }

    private void generateRefreshToken(String providerId) {
        try {
            jwtTokenProvider.reGenerateRefreshToken(providerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean validateAuthentication(String jwtToken) {
        return jwtTokenProvider.validateToken(jwtToken) && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void assignAuthenticationUser(Long userId) {
        AuthDetails authDetails = (AuthDetails) authService.loadUserByUsername(userId.toString());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            authDetails, null, authDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
