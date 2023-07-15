package teameight.gg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.dto.PostResponseDto;
import teameight.gg.entity.Dislike;
import teameight.gg.entity.Like;
import teameight.gg.entity.Post;
import teameight.gg.entity.User;
import teameight.gg.repository.DislikeRepository;
import teameight.gg.repository.LikeRepository;
import teameight.gg.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {

    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final PostRepository postRepository;

    public String updateLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다"));

        if (!isLikedPost(post, user)) {
            createLike(post, user);
            post.increaseLike();
            return "좋아요 성공";
        }

        removeLike(post, user);
        post.decreaseLike();
        return "좋아요 취소";
    }

    public String updateDislike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다"));

        if (!isDislikedPost(post, user)) {
            createDislike(post, user);
            post.increaseDislike();
            return "싫어요 성공";
        }

        removeDislike(post, user);
        post.decreaseDislike();
        return "싫어요 취소";
    }

    public boolean isLikedPost(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }

    public void createLike(Post post, User user) {
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    public void removeLike(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        likeRepository.delete(like);
    }

    public boolean isDislikedPost(Post post, User user) {
        return dislikeRepository.findByPostAndUser(post, user).isPresent();
    }

    public void createDislike(Post post, User user) {
        Dislike dislike = new Dislike(post, user);
        dislikeRepository.save(dislike);
    }

    public void removeDislike(Post post, User user) {
        Dislike dislike = dislikeRepository.findByPostAndUser(post, user).orElseThrow();
        dislikeRepository.delete(dislike);
    }
}
