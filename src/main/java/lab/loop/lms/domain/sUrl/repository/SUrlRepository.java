package lab.loop.lms.domain.sUrl.repository;

import lab.loop.lms.domain.sUrl.entity.SUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SUrlRepository extends JpaRepository<SUrl, Long> {
}
