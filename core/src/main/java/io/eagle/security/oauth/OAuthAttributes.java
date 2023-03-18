package io.eagle.security.oauth;


import io.eagle.entity.User;
import io.eagle.entity.type.ProviderType;
import io.eagle.entity.type.Ranks;
import io.eagle.entity.type.Role;
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

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .registrationId((String) attributes.get("sub"))
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
            .ranks(Ranks.NAMJAK)
            .cash(1000000L)
            .build();
    }
}