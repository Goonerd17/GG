package teameight.gg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.dto.PostResponseDto;
import teameight.gg.entity.Like;
import teameight.gg.entity.Post;
import teameight.gg.entity.User;
import teameight.gg.repository.LikeRepository;
import teameight.gg.repository.PostRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public PostResponseDto addLike(Long postId, User user) {
        Like like = new Like(user);
        Post post = findPost(postId);
        post.addLikeToPost(like);
        likeRepository.save(like);
        return new PostResponseDto(post);
    }

    public PostResponseDto checkLike(Long postId, User user) {
        Post post = findPost(postId);
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        if (!isAlreadyLike(post, user)) {
            likeRepository.save(like);
            post.addLikeToPost(like);
            return new PostResponseDto(post);
        }

        post.deleteLikeToPost(like);
        return new PostResponseDto(post);
    }

    private boolean isAlreadyLike(Post post, User user){
        return likeRepository.findByPostAndUser(post, user).isEmpty();
    }

    @Transactional(readOnly = true)
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시물은 존재하지 않습니다"));
    }
}
