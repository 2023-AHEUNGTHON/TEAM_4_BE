package aheung.likelion.twoneone.domain.community;

import aheung.likelion.twoneone.domain.common.BaseTimeEntity;
import aheung.likelion.twoneone.domain.enums.Category;
import aheung.likelion.twoneone.domain.user.User;
import aheung.likelion.twoneone.dto.community.PostRequestDto;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    Category category;

    String title;

    String memo;
    String link;

    boolean share;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "post")
    List<PostLike> postLikes;

    @OneToMany(mappedBy = "post")
    List<PostTag> postTags;

    public void updatePost(PostRequestDto dto) {
        this.category = dto.getCategory() != null ? Category.from(dto.getCategory()) : this.category;
        this.title = dto.getTitle() != null ? dto.getTitle() : this.getTitle();
        this.memo = dto.getMemo() != null ? dto.getMemo() : this.getMemo();
        this.link = dto.getLink() != null ? dto.getLink() : this.getLink();
        this.share = dto.isShare();
    }
}
