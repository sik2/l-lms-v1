package lab.loop.lms.domain.studyGroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/groups", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1StudyGroupsController", description = "스터디 그룹 컨트롤러")
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

    //    TODO: 그룹 리스트 불러오기(임시) 로그인된 유저의 리스트를 불러오는 것으로 바꿀 예정
    @GetMapping(value = "")
    @Operation(summary = "그룹 다건 불러오기")
    public RsData<GroupsResponse> groupList(Member member) {
        List<StudyGroup> studyGroups = this.studyGroupService.getStudyGroupByMemberId(member.getId()); // 로그인한 멤버가 생성한 스터디 그룹 리스트
        List<StudyGroup> invitedGroups = this.studyGroupService.getInvitedStudyGroupsByMemberId(member.getId()); // 로그인한 멤버가 초대 상태인 스터디 그룹 리스트
        if (studyGroups.isEmpty() && invitedGroups.isEmpty()) {
            return RsData.of("404", "스터디 그룹이 존재하지 않습니다.", null);
        }

        return RsData.of("200", "스터디 그룹 리스트 불러오기 성공", new GroupsResponse(studyGroups, invitedGroups));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "그룹 단건 불러오기")
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

    @PostMapping(value = "")
    @Operation(summary = "그룹 생성")
    public RsData<CreateResponse> createGroup(@Valid @RequestBody CreateRequest createRequest) {
        StudyGroup studyGroup = this.studyGroupService.createGroup(createRequest.getName(), createRequest.getDescription(), createRequest.getIsPublic());
        return RsData.of("200", "스터디 그룹을 생성하였습니다.", new CreateResponse(studyGroup));
    }

    //    그룹 수정
    @PatchMapping(value = "/{id}")
    @Operation(summary = "그룹 수정")
    public RsData<CreateResponse> modifyGroup(@PathVariable("id") Long id, @Valid @RequestBody CreateRequest createRequest) {
        StudyGroup studyGroup = this.studyGroupService.getStudyGroup(id);
        StudyGroup modifyStudyGroup = this.studyGroupService.modifyGroup(studyGroup, createRequest.getName(), createRequest.getDescription(), createRequest.getIsPublic());
        return RsData.of("200", "해당 스터디 그룹이 수정되었습니다.", new CreateResponse(modifyStudyGroup));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "그룹 삭제")
    public RsData<Void> deleteGroup(@PathVariable("id") Long id) {
        StudyGroup studyGroup = this.studyGroupService.getStudyGroup(id);
        this.studyGroupService.deleteGroup(studyGroup);
        return RsData.of("200", "해당 스터디 그룹이 삭제되었습니다.", null);
    }

    @PostMapping(value = "/{groupId}/members/{userId}")
    @Operation(summary = "그룹에 회원을 초대")
    public RsData<Void> inviteGroupMember(@PathVariable("groupId") Long groupId, @PathVariable("userId") Long userId) {

        studyGroupService.inviteGroupMember(groupId, userId);

        return RsData.of("200", "그룹에 회원 초대 성공", null);
    }

    @GetMapping(value = "/{groupId}/members")
    @Operation(summary = " 해당 그룹 회원 목록 리스트")
    public RsData<List<GroupMemberDto>> getGroupMemberList(@PathVariable("groupId") Long groupId) {

        List<GroupMemberDto> groupMemberList = studyGroupService.getGroupMemberList(groupId);

        if (groupMemberList.isEmpty()) {
            return RsData.of("400", "해당 그룹에 회원이 없습니다.", null);
        }

        return RsData.of("200", "그룹 회원 목록 불러오기 성공", groupMemberList);
    }
}
