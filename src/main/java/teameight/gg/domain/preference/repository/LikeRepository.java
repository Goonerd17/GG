package teameight.gg.domain.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teameight.gg.domain.preference.entity.Like;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);
}
