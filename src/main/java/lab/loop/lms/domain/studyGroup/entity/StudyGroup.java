package lab.loop.lms.domain.studyGroup.entity;

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
@SuperBuilder
@ToString(callSuper = true)
public class StudyGroup extends BaseEntity {
    @ManyToOne
    @JoinColumn
    private Member member;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean isPublic;
}
