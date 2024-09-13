package lab.loop.lms.domain.practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Practice extends BaseEntity {
    @ManyToOne
    @JoinColumn
    private Member member;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String langType;

    private Boolean isPublic;
}
