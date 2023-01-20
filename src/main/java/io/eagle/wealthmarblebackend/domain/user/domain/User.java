package io.eagle.wealthmarblebackend.domain.user.domain;

import io.eagle.wealthmarblebackend.domain.user.domain.type.ProviderType;
import io.eagle.wealthmarblebackend.domain.user.domain.type.Rank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
