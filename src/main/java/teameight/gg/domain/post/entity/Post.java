package teameight.gg.domain.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import teameight.gg.domain.comment.entity.Comment;
import teameight.gg.domain.post.dto.PostRequestDto;
import teameight.gg.domain.user.entity.User;
import teameight.gg.global.utils.Timestamped;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    private long liked;

    private long disliked;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = CASCADE)
    private User user;

    public Post(PostRequestDto postRequestDto, String image, User user) {
        this.title = postRequestDto.getTitle();
        this.username = user.getUsername();
        this.content = postRequestDto.getContent();
        this.image = image;
        this.liked = 0;
        this.disliked = 0;
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void updateAll(PostRequestDto postRequestDto, String image) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.image = image;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.initPost(this);
    }

    public void increaseLike() { this.liked += 1; }

    public void decreaseLike() {
        this.liked -= 1;
    }

    public void increaseDislike() {
        this.disliked += 1;
    }

    public void decreaseDislike() { this.disliked -= 1; }
}
