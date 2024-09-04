package lab.loop.lms.domain.studyGroup.repository;

import lab.loop.lms.domain.studyGroup.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<StudyGroup, Long> {
}
