package lab.loop.lms.domain.practice.dto;

import jakarta.validation.constraints.NotBlank;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.practice.entity.Practice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
public class PracticeDto {

    @NonNull
    private long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String langType;

    @NotBlank
    private Boolean isPublic;

    @NonNull
    private LocalDateTime createDate;

    @NonNull
    private LocalDateTime modifyDate;

    @NotBlank
    private Long userId;

    @NotBlank
    private String username;

    public PracticeDto(Practice practice) {
        this.id = practice.getId();
        this.title = practice.getTitle();
        this.content=practice.getContent();
        this.langType = practice.getLangType();
        this.isPublic= practice.getIsPublic();
        this.createDate = practice.getCreatedDate();
        this.modifyDate = practice.getModifiedDate();
        this.username = practice.getMember().getUsername();
        this.userId = practice.getMember().getId();
    }
}
