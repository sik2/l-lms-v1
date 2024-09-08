package lab.loop.lms.domain.studyGroup.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GroupMemberDto {

//    스터디 그룹 id
    private Long studyGroupId;

//    멤버 id
    private Long memberId;

//    멤버 레벨
    private String level;

//    초대 상태 (Enum 타입은 Dto에서 문자열로 처리해야함)
    private String invitationStatus;

//    밴당한 자식들 여부
    private Boolean isBanned;
}
