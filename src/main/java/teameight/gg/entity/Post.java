package teameight.gg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teameight.gg.dto.PostRequestDto;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

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

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Post(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.username = user.getUsername();
        this.content = postRequestDto.getContent();
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void addLikeToPost(Like like) {
        likes.add(like);
        like.setPost(this);
    }

    public void deleteLikeToPost(Like like) {
        likes.remove(like);
        like.setPost(this);
    }
}
