package io.eagle.domain.user.dto.response;

import io.eagle.entity.User;
import io.eagle.entity.type.ProviderType;
import io.eagle.entity.type.Ranks;
import io.eagle.entity.type.Role;
import lombok.Getter;

@Getter
public class UserInfoDto {

    private String username;
    private Long cash;
    private Integer value;
    private String email;
    private ProviderType providerType;
    private Ranks rank;
    private Role role;


    public UserInfoDto(Integer value, User user) {
        this.username = user.getNickname();
        this.cash = user.getCash();
        this.value = value;
        this.email = user.getEmail();
        this.providerType = user.getProviderType();
        this.rank = user.getRanks();
        this.role = user.getRole();
    }

}
