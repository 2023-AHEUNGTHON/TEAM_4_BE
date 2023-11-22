package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.community.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
