package lab.loop.lms.domain.studyGroup.service;

import jakarta.persistence.EntityNotFoundException;
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

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyRepository studyRepository;
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
}
