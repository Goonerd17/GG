package teameight.gg.domain.preference.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static org.hibernate.annotations.OnDeleteAction.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = CASCADE)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
