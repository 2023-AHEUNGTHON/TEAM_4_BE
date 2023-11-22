package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.community.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
