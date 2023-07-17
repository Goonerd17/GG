package teameight.gg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.dto.CommentRequestDto;
import teameight.gg.entity.Comment;
import teameight.gg.entity.Post;
import teameight.gg.entity.User;
import teameight.gg.repository.CommentRepository;
import teameight.gg.repository.PostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public String createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = new Comment(commentRequestDto, user);
        findPost(postId).addComment(comment);
        commentRepository.save(comment);
        return "댓글 작성 완료";
    }

    public String updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(commentId);
        checkUsername(commentId, user);
        comment.update(commentRequestDto);
        return "댓글 수정 완료";
    }

    public String deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        checkUsername(commentId, user);
        commentRepository.delete(comment);
        return "댓글 삭제 성공";
    }

    @Transactional
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글은 존재하지 않습니다"));
    }

    @Transactional
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글은 존재하지 않습니다"));
    }

    private void checkUsername(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if (!(comment.getUser().getId() == user.getId())) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }

    }
}
