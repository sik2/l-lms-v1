package lab.loop.lms.domain.studyGroup.service;
import jakarta.persistence.EntityNotFoundException;
import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.member.repository.MemberRepository;
import lab.loop.lms.domain.studyGroup.dto.GroupMemberDto;
import lab.loop.lms.domain.studyGroup.entity.GroupMember;
import lab.loop.lms.domain.studyGroup.entity.StudyGroup;
import lab.loop.lms.domain.studyGroup.repository.GroupMemberRepository;
import lab.loop.lms.domain.studyGroup.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lab.loop.lms.domain.studyGroup.studyGroupConvertDto.StudyGroupDtoConverter;
import java.util.stream.Collectors;
import static lab.loop.lms.domain.studyGroup.entity.GroupMember.InvitationStatus.INVITED;
import static lab.loop.lms.domain.studyGroup.entity.GroupMember.LevelStatus.MEMBER;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    public List<StudyGroup> getStudyGroupByMemberId(Long memberId) {
        return this.studyRepository.findAllByMemberId(memberId);
    }

    public List<StudyGroup> getInvitedStudyGroupsByMemberId(Long memberId) {
        List<GroupMember> groupMembers = groupMemberRepository.findByMemberIdAndInvitationStatus(memberId, GroupMember.InvitationStatus.INVITED);

        List<StudyGroup> studyGroups = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            studyGroups.add(groupMember.getStudyGroup());
        }

        return studyGroups;
    }

    public List<StudyGroup> getStudyGroups() {
        return this.studyRepository.findAll();
    }

    public StudyGroup createGroup(String name, String description, Boolean isPublic) {
        StudyGroup studyGroup = StudyGroup.builder()
                .name(name)
                .description(description)
                .isPublic(isPublic)
                .createdDate(LocalDateTime.now())
                .build();
        return this.studyRepository.save(studyGroup);
    }

    public StudyGroup getStudyGroup(Long id) {
        return this.studyRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 그룹이 존재하지 않습니다."));
    }

    public StudyGroup modifyGroup(StudyGroup studyGroup, String name, String description, Boolean isPublic) {
        studyGroup.setName(name);
        studyGroup.setDescription(description);
        studyGroup.setIsPublic(isPublic);

        return this.studyRepository.save(studyGroup);
    }

    public void deleteGroup(StudyGroup studyGroup) {
        if(!studyRepository.existsById(studyGroup.getId())) {
            throw new EntityNotFoundException("스터디 그룹이 존재하지 않습니다");
        }

        try {
            this.studyRepository.delete(studyGroup);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("삭제가 불가합니다", e);
        }
    }



    //    그룹에 회원을 초대하는 구문
    public void inviteGroupMember (Long groupId, Long userId) {
//        그룹과 사용자를 조회
        StudyGroup studyGroup = studyRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 그룹을 찾을 수 없습니다."));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

//        이미 초대되어있는지 확인
        if (groupMemberRepository.existsByStudyGroupAndMember(studyGroup, member)) {
            throw new IllegalArgumentException("이미 초대된 사용자입니다.");
        }

        GroupMember groupMember = GroupMember.builder()
                .studyGroup(studyGroup)
                .member(member)
                .levelStatus(MEMBER) // 기본값으로 MEMBER 설정
                .invitationStatus(INVITED) // 기본값으로 초대 상태로 설정
                .isBanned(false) // 기본값으로 차단되지 않은 상태로 설정
                .build();

        groupMemberRepository.save(groupMember);
    }

//    해당 그룹 회원 목록을 조회하는 구문
    public List<GroupMemberDto> getGroupMemberList(Long groupId) {
//        스터디 그룹 찾는 구문
        StudyGroup studyGroup = studyRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

//        해당 그룹에 속한 멤버들 조회
        List<GroupMember> groupMemberList = groupMemberRepository.findByStudyGroup(studyGroup);

//        Member 엔티티 리스트를 GroupMemberDto 리스트로 변환
        return groupMemberList.stream()
                .map(StudyGroupDtoConverter::convertToDto) // 변환 메서드를 사용
                .collect(Collectors.toList());
    }
}
