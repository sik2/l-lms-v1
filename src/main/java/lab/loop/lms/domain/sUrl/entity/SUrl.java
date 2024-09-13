package lab.loop.lms.domain.sUrl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.global.jpa.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SUrl extends BaseEntity {

    @ManyToOne
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String originUrl;

    private String shortUrl;

    private Long redirectCount;

    private Boolean isActive;
}
