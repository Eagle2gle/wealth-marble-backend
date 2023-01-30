package io.eagle.wealthmarblebackend.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eagle.wealthmarblebackend.domain.user.dto.CreateUserDto;
import io.eagle.wealthmarblebackend.domain.user.mapper.UserRequestMapper;
import io.eagle.wealthmarblebackend.security.jwt.JwtTokenProvider;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        CreateUserDto createUserDto = userRequestMapper.toCreateUserDto(oAuth2User);

        Claims claims = Jwts.claims().setSubject(createUserDto.getNickname());
        claims.put("role", "USER");

        Map<String, String> token = jwtTokenProvider.generateTokenSet(createUserDto.getNickname(), claims);
        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, Map<String, String> token) throws IOException {
        String accessToken = token.get("accessToken");
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Authorization", "Bearer " + accessToken);
    }
}
