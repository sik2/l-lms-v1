package lab.loop.lms.domain.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lab.loop.lms.domain.member.entity.Member;
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
public class Post extends BaseEntity {
    @ManyToOne
    private Member author;
    private String subject;
    private String content;
}