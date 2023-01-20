package io.eagle.wealthmarblebackend.domain.user.entity;

import io.eagle.wealthmarblebackend.domain.user.entity.type.ProviderType;
import io.eagle.wealthmarblebackend.domain.user.entity.type.Rank;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @Enumerated
    private ProviderType providerType;

    @NotNull
    private Integer providerId;

    @Enumerated
    private Rank rank = Rank.NAMJAK;
    private String refreshToken;
    private Integer cash = 0;
}
