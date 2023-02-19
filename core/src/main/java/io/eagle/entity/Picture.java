package io.eagle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.eagle.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private <User|Vacation> foreignId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cahoots_id")
    private Vacation vacation;
    private String type;
    private String url;

    @Builder
    public Picture(String url, String type, Vacation vacation ){
        this.url = url;
        this.type = type;
        this.vacation = vacation;
    }
}