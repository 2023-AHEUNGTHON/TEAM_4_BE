package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.community.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
