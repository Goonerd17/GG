package teameight.gg.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teameight.gg.global.responseDto.ApiResponse;
import teameight.gg.domain.comment.dto.CommentRequestDto;
import teameight.gg.global.security.UserDetailsImpl;
import teameight.gg.domain.comment.service.CommentService;

import static teameight.gg.global.utils.ResponseUtils.ok;

@RestController
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<?> createComment(@PathVariable Long postId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(commentService.createComment(postId, commentRequestDto, userDetailsImpl.getUser()));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<?> updateComment(@PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(commentService.updateComment(commentId, commentRequestDto, userDetailsImpl.getUser()));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<?> removeComment(@PathVariable Long commentId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(commentService.deleteComment(commentId, userDetailsImpl.getUser()));
    }
}
