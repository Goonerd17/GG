package teameight.gg.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teameight.gg.domain.comment.dto.CommentRequestDto;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;
import teameight.gg.global.utils.Timestamped;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Comment extends Timestamped {

    @Id // 식별자
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, User user) {
        this.content = commentRequestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    public void initPost(Post post) {
        this.post = post;
    }
}