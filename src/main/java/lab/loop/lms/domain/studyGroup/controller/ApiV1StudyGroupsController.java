package lab.loop.lms.domain.studyGroup.controller;

import lab.loop.lms.domain.studyGroup.dto.GroupMemberDto;
import lab.loop.lms.domain.studyGroup.service.StudyGroupService;
import lab.loop.lms.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/groups")
public class ApiV1StudyGroupsController {

    private final StudyGroupService studyGroupService;

//    그룹에 회원을 초대하는 구문
    @PostMapping(value = "/{groupId}/members/{userId}")
    public RsData<?> inviteGroupMember(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {

        studyGroupService.inviteGroupMember(groupId, userId);

        return RsData.of("S-1", "그룹에 회원 초대 성공", null);
    }

//    해당 그룹 회원 목록 리스트 불러오는 구문
    @GetMapping(value = "/{groupId}/members")
    public RsData<?> getGroupMemberList(@PathVariable("groupId") Long groupId) {

        List<GroupMemberDto> groupMemberList = studyGroupService.getGroupMemberList(groupId);

        if (groupMemberList.isEmpty()) {
            return RsData.of("F-1", "해당 그룹에 회원이 없습니다.", null);
        }

        return RsData.of("S-1", "그룹 회원 목록 불러오기 성공", groupMemberList);
    }
}
