package lab.loop.lms.domain.studyGroup.repository;

import lab.loop.lms.domain.studyGroup.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
//    로그인된 유저가 초대가 된 스터디그룹의 리스트 불러오기
    List<GroupMember> findByMemberIdAndInvitationStatus(Long memberId, GroupMember.InvitationStatus invitationStatus);
}
