package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.enums.Category;
import aheung.likelion.twoneone.domain.user.User;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> getPostByCategoryAndUser(Category category, User user, Sort sort);
    List<Post> getPostByTitleContainingAndUser(String title, User user, Sort sort);
    List<Post> getPostByTitleContainingAndShareIsTrue(String title, Sort sort);
    List<Post> getPostByShareIsTrue(Sort sort);
}
