package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.community.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
