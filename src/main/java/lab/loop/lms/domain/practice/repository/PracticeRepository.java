package lab.loop.lms.domain.practice.repository;

import lab.loop.lms.domain.practice.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository  extends JpaRepository<Practice, Long> {
}
