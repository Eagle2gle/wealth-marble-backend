package io.eagle.domain.user.mapper;

import io.eagle.domain.user.dto.CreateUserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRequestMapper {

    public CreateUserDto toCreateUserDto(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return CreateUserDto.builder()
            .providerId((String) attributes.get("sub"))
            .email((String) attributes.get("email"))
            .nickname((String) attributes.get("name"))
            .build();
    }

}
