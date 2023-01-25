package io.eagle.wealthmarblebackend.security.jwt;

import io.eagle.wealthmarblebackend.domain.user.entity.User;
import io.eagle.wealthmarblebackend.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
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

    // Token 만료 일자 조회
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Token 만료 여부 확인
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // access token 생성
    public String generateAccessToken(String id, Map<String, Object> claims) {
        return doGenerateAccesssToken(id, claims);
    }

    private String doGenerateAccesssToken(String id, Map<String, Object> claims) {
        String accessToken = Jwts.builder()
            .setClaims(claims)
            .setId(id)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
        return accessToken;
    }

    // id로 refreshToken 생성
    public String generateRefreshToken(String id) {
        return doGenerateRefreshToken(id);
    }

    private String doGenerateRefreshToken(String id) {
        String refreshToken = Jwts.builder()
            .setId(id)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
        return refreshToken;
    }

    // accessToken, refreshToken 동시 생성
    public Map<String, String> generateTokenSet(String id, Map<String, Object> claims) {
        return doGenerateTokenSet(id, claims);
    }

    private Map<String, String> doGenerateTokenSet(String id, Map<String, Object> claims) {
        Map<String, String> tokens = new HashMap<String, String>();

        String accessToken = doGenerateAccesssToken(id, claims);
        String refreshToken = doGenerateRefreshToken(id);

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    // JWT refreshToken 만료체크 후 재발급
    public Boolean reGenerateRefreshToken(String id) throws Exception {
        // id 즉, nickname 으로 User 조회
        User user = userRepository.findByNickname(id).orElse(null);

        // User가 존재하지 않거나 refresh 토큰이 존재하지 않을 경우
        if (user == null || user.getRefreshToken() == null) {
            return false;
        }

        try {
            String refreshToken = user.getRefreshToken().substring(7);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            user.setRefreshToken("Bearer " + generateRefreshToken(id));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
