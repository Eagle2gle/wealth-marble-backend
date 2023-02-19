package io.eagle.security.jwt;

import io.eagle.entity.User;
import io.eagle.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;
    private String secretKey = "wealth-marble";
    private long TOKEN_VALID_TIME = 60 * 60 * 1000L; // 1시간

    // 객체 초기화 후 secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 모든 Token에 대한 사용자 정보 조회
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // Token으로 사용자 속성 정보 조회
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Token에서 사용자 정보 조회
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(getAllClaimsFromToken(token).get("id").toString());
    }

    // accessToken, refreshToken 동시 생성
    public String generateTokenSet(String providerId, Map<String, Object> claims) {
        saveRefreshToken(providerId);
        return generateAccessToken(providerId, claims);
    }

    // access token 생성
    private String generateAccessToken(String providerId, Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setId(providerId)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    // id로 refreshToken 생성
    private String generateRefreshToken(String providerId) {
        return Jwts.builder()
            .setId(providerId)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME * 24 * 7))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    // JWT refreshToken 만료체크 후 재발급
    public Boolean reGenerateRefreshToken(String providerId) throws Exception {
        // id 즉, providerId 으로 User 조회
        User user = userRepository.findUserByProviderId(providerId).orElse(null);

        // User가 존재하지 않거나 refresh 토큰이 존재하지 않을 경우
        if (user == null || user.getRefreshToken() == null) {
            return false;
        }

        try {
            String refreshToken = user.getRefreshToken();
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            user.setRefreshToken(generateRefreshToken(providerId));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveRefreshToken(String providerId) {
        User user = userRepository.findUserByProviderId(providerId).orElse(null);
        if (user != null) {
            user.setRefreshToken(generateRefreshToken(providerId));
            userRepository.save(user);
        }
    }

    // 토큰 검증
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
