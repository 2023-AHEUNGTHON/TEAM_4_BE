package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.community.PostTag;
import aheung.likelion.twoneone.domain.community.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    boolean existsByPostAndTag(Post post, Tag tag);
}
