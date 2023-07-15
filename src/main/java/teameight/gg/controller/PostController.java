package teameight.gg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teameight.gg.dto.ApiResponse;
import teameight.gg.dto.PostRequestDto;
import teameight.gg.dto.PostSearchCondition;
import teameight.gg.security.UserDetailsImpl;
import teameight.gg.service.PostService;

import static teameight.gg.utils.ResponseUtils.ok;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return ok(postService.searchPost(condition, pageable));
    }

    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId) {
        return ok(postService.getSinglePost(postId));
    }

    @PostMapping("/newpost")
    public ApiResponse<?> createPost(@RequestBody PostRequestDto postRequestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(postService.createPost(postRequestDto, userDetailsImpl.getUser()));
    }

    @PutMapping("/{postId}")
    public ApiResponse<?> modifyPost(@PathVariable Long postId,
                                     @RequestBody PostRequestDto postRequestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(postService.updatePost(postId, postRequestDto, userDetailsImpl.getUser()));
    }

    @DeleteMapping ("/{postId}")
    public ApiResponse<?> removePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(postService.deletePost(postId, userDetailsImpl.getUser()));
    }
}