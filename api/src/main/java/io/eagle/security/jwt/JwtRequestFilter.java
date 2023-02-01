package io.eagle.security.jwt;

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
    private final UserDetailsService userDetailsService;

    // 인증에서 제외할 URL
    private static final List<String> EXCLUDE_URL = Collections.unmodifiableList(
        Arrays.asList(
            "/admin"
        )
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Local Storage 사용
        final String token = request.getHeader("Authorization");

        String jwtToken = null;
        String userId = null;

        if (token != null && token.startsWith("Bearer ")) {
            jwtToken = token.substring(7);

            try {
                userId = jwtTokenProvider.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (userId != null && jwtTokenProvider.validateToken(jwtToken) && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) userDetailsService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Access Token 인증 후 refresh Token 재발급
        try {
            if (userId != null) {
                jwtTokenProvider.reGenerateRefreshToken(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
