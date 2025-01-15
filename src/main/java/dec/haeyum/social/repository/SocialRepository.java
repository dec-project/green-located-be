package dec.haeyum.social.repository;

import dec.haeyum.social.entity.SocialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialRepository extends JpaRepository<SocialEntity,Long> {

    Optional<SocialEntity> findBySocialSub(String sub);
}
