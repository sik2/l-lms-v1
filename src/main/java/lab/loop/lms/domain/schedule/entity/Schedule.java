package lab.loop.lms.domain.schedule.entity;

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
public class Schedule extends BaseEntity {
    private String name;
}
