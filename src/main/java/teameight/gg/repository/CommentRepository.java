package teameight.gg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teameight.gg.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
