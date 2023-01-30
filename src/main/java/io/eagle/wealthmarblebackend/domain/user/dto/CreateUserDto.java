package io.eagle.wealthmarblebackend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateUserDto {

    private String email;
    private String nickname;

    @Builder
    public CreateUserDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

}
