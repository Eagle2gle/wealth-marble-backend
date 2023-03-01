package io.eagle.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
import io.eagle.security.dto.CreateUserDto;
import io.eagle.security.jwt.JwtTokenProvider;
import io.eagle.security.mapper.UserRequestMapper;
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
    private final UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        CreateUserDto createUserDto = userRequestMapper.toCreateUserDto(oAuth2User);

        User user = userRepository.findUserByProviderId(createUserDto.getProviderId()).orElse(null);
        if (user != null) {
            Claims claims = Jwts.claims().setSubject(createUserDto.getProviderId());
            claims.put("role", "ROLE_USER");
            claims.put("id", user.getId());

            String accessToken = jwtTokenProvider.generateTokenSet(createUserDto.getProviderId(), claims);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + accessToken);
        }
    }
}
