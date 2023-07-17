package teameight.gg.domain.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teameight.gg.domain.preference.entity.Dislike;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {

    Optional<Dislike> findByPostAndUser(Post post, User user);
}
