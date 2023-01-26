package io.eagle.wealthmarblebackend.security.oauth;


import io.eagle.wealthmarblebackend.domain.user.entity.User;
import io.eagle.wealthmarblebackend.domain.user.entity.type.ProviderType;
import io.eagle.wealthmarblebackend.domain.user.entity.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String registrationId;
    private String nickname;
    private String email;
    private Role role;
    private ProviderType providerType;

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {

        return ofGoogle(userNameAttributeName, attributes, registrationId);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, String registrationId) {
        return OAuthAttributes.builder()
            .registrationId(registrationId)
            .email((String) attributes.get("email"))
            .nickname((String) attributes.get("name"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .providerType(ProviderType.GOOGLE)
            .build();
    }

    public User toEntity() {
        return User.builder()
            .email(email)
            .nickname(nickname)
            .providerId(registrationId)
            .providerType(ProviderType.GOOGLE)
            .role(Role.USER)
            .build();
    }
}