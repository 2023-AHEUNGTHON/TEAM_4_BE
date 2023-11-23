package aheung.likelion.twoneone.dto.community;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.enums.Category;
import aheung.likelion.twoneone.domain.user.User;
import lombok.Data;

@Data
public class PostRequestDto {
    private String category;
    private String title;
    private String memo;
    private String link;
    private boolean share;

    public Post toEntity(User user) {
        return Post.builder()
                .category(Category.from(this.category))
                .title(this.title)
                .memo(this.memo)
                .link(this.link)
                .share(this.share)
                .user(user)
                .build();
    }
}
