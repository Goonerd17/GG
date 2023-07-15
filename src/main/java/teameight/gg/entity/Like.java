package teameight.gg.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes")
public class Like {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected void setPost(Post post) {
        this.post = post;
    }

    public Like(User user) {
        this.user = user;
    }
    public Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
