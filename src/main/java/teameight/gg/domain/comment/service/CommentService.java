package teameight.gg.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.domain.comment.dto.CommentRequestDto;
import teameight.gg.domain.comment.entity.Comment;
import teameight.gg.domain.comment.repository.CommentRepository;
import teameight.gg.domain.post.entity.Post;
import teameight.gg.domain.user.entity.User;
import teameight.gg.domain.post.repository.PostRepository;
import teameight.gg.global.exception.InvalidConditionException;
import teameight.gg.global.responseDto.ApiResponse;

import static teameight.gg.global.stringCode.ErrorCodeEnum.*;
import static teameight.gg.global.stringCode.SuccessCodeEnum.*;
import static teameight.gg.global.utils.ResponseUtils.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ApiResponse<?> createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = new Comment(commentRequestDto, user);
        findPost(postId).addComment(comment);
        commentRepository.save(comment);
        return okWithMessage(COMMENT_CREATE_SUCCESS);
    }

    public ApiResponse<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(commentId);
        checkUsername(commentId, user);
        comment.update(commentRequestDto);
        return okWithMessage(COMMENT_UPDATE_SUCCESS);
    }

    public ApiResponse<?> deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        checkUsername(commentId, user);
        commentRepository.delete(comment);
        return okWithMessage(COMMENT_DELETE_SUCCESS);
    }

    @Transactional(readOnly = true)
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new InvalidConditionException(COMMENT_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private void checkUsername(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if (!(comment.getUser().getId() == user.getId())) {
            throw new InvalidConditionException(USER_NOT_MATCH);
        }
    }
}