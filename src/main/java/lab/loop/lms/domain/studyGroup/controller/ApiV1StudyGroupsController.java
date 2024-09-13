package lab.loop.lms.domain.studyGroup.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.studyGroup.entity.StudyGroup;
import lab.loop.lms.domain.studyGroup.service.StudyGroupService;
import lab.loop.lms.global.rsData.RsData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lab.loop.lms.domain.studyGroup.dto.GroupMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/groups")
public class ApiV1StudyGroupsController {

    private final StudyGroupService studyGroupService;

    @Getter
    @AllArgsConstructor
    public static class GroupsResponse {
        private final List<StudyGroup> studyGroups;
        private final List<StudyGroup> invitedGroups;
    }

    @Getter
    @AllArgsConstructor
    public static class GroupResponse {
        private final StudyGroup studyGroup;
    }

    //    그룹 리스트 불러오기(임시) 로그인된 유저의 리스트를 불러오는 것으로 바꿀 예정
    @GetMapping(value = "")
    public RsData<GroupsResponse> groupList(Member member) {
        List<StudyGroup> studyGroups = this.studyGroupService.getStudyGroupByMemberId(member.getId()); // 로그인한 멤버가 생성한 스터디 그룹 리스트
        List<StudyGroup> invitedGroups = this.studyGroupService.getInvitedStudyGroupsByMemberId(member.getId()); // 로그인한 멤버가 초대 상태인 스터디 그룹 리스트
        if (studyGroups.isEmpty() && invitedGroups.isEmpty()) {
            return RsData.of("404", "스터디 그룹이 존재하지 않습니다.", null);
        }

        return RsData.of("200", "스터디 그룹 리스트 불러오기 성공", new GroupsResponse(studyGroups, invitedGroups));
    }

    //    그룹 불러오기
    @GetMapping(value = "/{id}")
    public RsData<GroupResponse> getStudyGroup(@PathVariable("id") Long id) {
        StudyGroup studyGroup = this.studyGroupService.getStudyGroup(id);
        if (studyGroup == null) {
            throw new EntityNotFoundException("해당 스터디 그룹이 존재하지 않습니다.");
        }

        return RsData.of("200", "스터디 그룹 불러오기 성공", new GroupResponse(studyGroup));
    }

    @Data
    public static class CreateRequest {
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotNull
        private Boolean isPublic;
    }
    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private final StudyGroup studyGroup;
    }
    //    그룹 생성
    @PostMapping(value = "")
    public RsData<CreateResponse> createGroup(@Valid @RequestBody CreateRequest createRequest) {
        StudyGroup studyGroup = this.studyGroupService.createGroup(createRequest.getName(), createRequest.getDescription(), createRequest.getIsPublic());
        return RsData.of("200", "스터디 그룹을 생성하였습니다.", new CreateResponse(studyGroup));
    }

    //    그룹 수정
    @PatchMapping(value = "/{id}")
    public RsData<CreateResponse> modifyGroup(@PathVariable("id") Long id, @Valid @RequestBody CreateRequest createRequest) {
        StudyGroup studyGroup = this.studyGroupService.getStudyGroup(id);
        StudyGroup modifyStudyGroup = this.studyGroupService.modifyGroup(studyGroup, createRequest.getName(), createRequest.getDescription(), createRequest.getIsPublic());
        return RsData.of("200", "해당 스터디 그룹이 수정되었습니다.", new CreateResponse(modifyStudyGroup));
    }

    //    그룹 삭제
    @DeleteMapping(value = "/{id}")
    public RsData<Void> deleteGroup(@PathVariable("id") Long id) {
        StudyGroup studyGroup = this.studyGroupService.getStudyGroup(id);
        this.studyGroupService.deleteGroup(studyGroup);
        return RsData.of("200", "해당 스터디 그룹이 삭제되었습니다.", null);
    }
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
