package lab.loop.lms.domain.studyGroup.studyGroupConvertDto;

import lab.loop.lms.domain.studyGroup.dto.GroupMemberDto;
import lab.loop.lms.domain.studyGroup.entity.GroupMember;

//    엔티티를 받아서 Dto로 변환해주는 Convert구문, 쉽게 DTO로 변환하기 위해 사용하는 구문
public class StudyGroupDtoConverter {

    // GroupMember 엔티티를 GroupMemberDto로 변환하는 메서드
    public static GroupMemberDto convertToDto(GroupMember groupMember) {
        return GroupMemberDto.builder()
                .studyGroupId(groupMember.getStudyGroup().getId()) // StudyGroup의 ID
                .memberId(groupMember.getMember().getId())         // Member의 ID
                .level(groupMember.getLevelStatus().name())        // Enum LevelStatus -> 문자열로 변환
                .invitationStatus(groupMember.getInvitationStatus().name()) // Enum InvitationStatus -> 문자열로 변환
                .isBanned(groupMember.getIsBanned())               // isBanned 상태
                .build();
    }
}
