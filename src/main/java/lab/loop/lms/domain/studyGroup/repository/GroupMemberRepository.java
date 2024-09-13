package lab.loop.lms.domain.studyGroup.repository;

import lab.loop.lms.domain.member.entity.Member;
import lab.loop.lms.domain.studyGroup.entity.GroupMember;
import lab.loop.lms.domain.studyGroup.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
//    해당 스터디 그룹에 해당 멤버가 존재하는지 확인하는 구문
    boolean existsByStudyGroupAndMember(StudyGroup studyGroup, Member member);

//    해당 스터디 그룹에 속한 멤버들 조회하는 구문
    List<GroupMember> findByStudyGroup(StudyGroup studyGroup);
//    로그인된 유저가 초대가 된 스터디그룹의 리스트 불러오기
    List<GroupMember> findByMemberIdAndInvitationStatus(Long memberId, GroupMember.InvitationStatus invitationStatus);
}
