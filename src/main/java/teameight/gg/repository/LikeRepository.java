package teameight.gg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teameight.gg.entity.Like;
import teameight.gg.entity.Post;
import teameight.gg.entity.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);
}
