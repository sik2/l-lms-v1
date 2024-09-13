package lab.loop.lms.domain.studyGroup.repository;

import lab.loop.lms.domain.studyGroup.entity.GroupMember;
import lab.loop.lms.domain.studyGroup.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<StudyGroup, Long> {

//    멤버 아이디로 스터디 그룹 리스트 불러오기
    List<StudyGroup> findAllByMemberId(long memberId);
}
