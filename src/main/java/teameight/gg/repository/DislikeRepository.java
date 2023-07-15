package teameight.gg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teameight.gg.entity.Dislike;
import teameight.gg.entity.Post;
import teameight.gg.entity.User;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {

    Optional<Dislike> findByPostAndUser(Post post, User user);
}
