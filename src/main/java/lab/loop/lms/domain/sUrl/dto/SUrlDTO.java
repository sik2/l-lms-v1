package lab.loop.lms.domain.sUrl.dto;

import jakarta.validation.constraints.NotNull;
import lab.loop.lms.domain.sUrl.entity.SUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SUrlDTO {
    private Long id;

//    // 작성자 id값
//    private Long memberId;
//    // 작성자 이름(닉네임)
//    private String memberName;
    // 원래 URL
    @NotNull
    private String originUrl;
    // 변경된 짧은 URL
    private String shortUrl;
    // 리다이렉트 카운트
    private Long redirectCount;
    // 활성화
    private Boolean isActive;
    // 생성일
    private LocalDateTime createDate;
    // 수정일
    private LocalDateTime modifiedDate;

    public SUrlDTO(SUrl sUrl) {
        this.id = sUrl.getId();
//        this.memberId = sUrl.getAuthor().getId();
//        this.memberName = sUrl.getAuthor().getNickname();
        this.originUrl = sUrl.getOriginUrl();
        this.shortUrl = sUrl.getShortUrl();
        this.redirectCount = sUrl.getRedirectCount();
        this.isActive = sUrl.getIsActive();
        this.createDate = sUrl.getCreatedDate();
        this.modifiedDate = sUrl.getModifiedDate();
    }
}
