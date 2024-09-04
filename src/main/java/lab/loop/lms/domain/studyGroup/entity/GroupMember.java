package lab.loop.lms.domain.studyGroup.entity;

import jakarta.persistence.*;
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
public class GroupMember extends BaseEntity {
    @ManyToOne
    @JoinColumn
    private StudyGroup studyGroup;

    @ManyToOne
    @JoinColumn
    private Member member;

    private Byte level;

    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;

    public enum InvitationStatus {
        INVITED,
        NOT_INVITED,
        PENDING,
        REJECT
    }

    private Boolean isBanned;
}
