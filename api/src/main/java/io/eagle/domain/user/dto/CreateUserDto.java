package io.eagle.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateUserDto {

    private String providerId;
    private String email;
    private String nickname;

    @Builder
    public CreateUserDto(String providerId, String email, String nickname) {
        this.providerId = providerId;
        this.email = email;
        this.nickname = nickname;
    }

}
