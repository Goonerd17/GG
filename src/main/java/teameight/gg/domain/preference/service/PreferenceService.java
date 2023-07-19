package teameight.gg.domain.preference.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.domain.preference.entity.Dislike;
import teameight.gg.domain.preference.entity.Like;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;
import teameight.gg.domain.preference.repository.DislikeRepository;
import teameight.gg.domain.preference.repository.LikeRepository;
import teameight.gg.domain.post.repository.PostRepository;
import teameight.gg.global.exception.InvalidConditionException;
import teameight.gg.global.responseDto.ApiResponse;

import static teameight.gg.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;
import static teameight.gg.global.stringCode.SuccessCodeEnum.*;
import static teameight.gg.global.utils.ResponseUtils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {

    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final PostRepository postRepository;

    public ApiResponse<?> updateLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new InvalidConditionException(POST_NOT_EXIST));;

        if (!isLikedPost(post, user)) {
            createLike(post, user);
            post.increaseLike();
            return okWithMessage(LIKE_SUCCESS);
        }

        removeLike(post, user);
        post.decreaseLike();
        return okWithMessage(LIKE_CANCEL_SUCCESS);
    }

    public ApiResponse<?> updateDislike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new InvalidConditionException(POST_NOT_EXIST));;

        if (!isDislikedPost(post, user)) {
            createDislike(post, user);
            post.increaseDislike();
            return okWithMessage(DISLIKE_SUCCESS);
        }

        removeDislike(post, user);
        post.decreaseDislike();
        return okWithMessage(DISLIKE_CANCEL_SUCCESS);
    }

    private boolean isLikedPost(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }

    private boolean isDislikedPost(Post post, User user) {
        return dislikeRepository.findByPostAndUser(post, user).isPresent();
    }

    private void createLike(Post post, User user) {
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    private void createDislike(Post post, User user) {
        Dislike dislike = new Dislike(post, user);
        dislikeRepository.save(dislike);
    }

    private void removeLike(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        likeRepository.delete(like);
    }

    private void removeDislike(Post post, User user) {
        Dislike dislike = dislikeRepository.findByPostAndUser(post, user).orElseThrow();
        dislikeRepository.delete(dislike);
    }
}
