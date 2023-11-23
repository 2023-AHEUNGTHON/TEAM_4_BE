package aheung.likelion.twoneone.dto.community;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.enums.Category;
import com.sun.xml.bind.v2.TODO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostListReturnDto {
    private Long id;
    private Category category;
    private String title;
    private int likes;
    private boolean likeStatus;
    private String thumbnail;
    private String createdAt;

    public static PostListReturnDto toDto(Post post, String thumbnail, boolean likeStatus) {
        return PostListReturnDto.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .likes(post.getLikes())
                .likeStatus(likeStatus)
                .createdAt(post.getCreatedAtToString())
                .thumbnail(thumbnail)
                .build();
    }
}
