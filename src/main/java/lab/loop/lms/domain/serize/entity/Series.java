package lab.loop.lms.domain.serize.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lab.loop.lms.global.jpa.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Series extends BaseEntity {
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String refreshToken;
}
