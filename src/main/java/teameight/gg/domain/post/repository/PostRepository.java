package teameight.gg.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teameight.gg.domain.post.entity.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{

    @Query("select p from Post p where p.id = :postId")
    Optional<Post> findDetailPost(@Param("postId") Long postId);
}
