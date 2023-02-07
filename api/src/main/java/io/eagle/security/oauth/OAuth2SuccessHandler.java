package io.eagle.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eagle.common.ApiResponse;
import io.eagle.domain.user.dto.CreateUserDto;
import io.eagle.domain.user.mapper.UserRequestMapper;
import io.eagle.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRequestMapper userRequestMapper;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        CreateUserDto createUserDto = userRequestMapper.toCreateUserDto(oAuth2User);

        Claims claims = Jwts.claims().setSubject(createUserDto.getNickname());
        claims.put("role", "USER");

        String accessToken = jwtTokenProvider.generateTokenSet(createUserDto.getNickname(), claims);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writeValueAsString(ApiResponse.createSuccess(accessToken)));
        response.sendRedirect("http://localhost:5000/login/oauth");
    }
}
