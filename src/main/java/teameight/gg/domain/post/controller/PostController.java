package teameight.gg.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teameight.gg.global.responseDto.ApiResponse;
import teameight.gg.domain.post.dto.PostRequestDto;
import teameight.gg.domain.post.dto.PostSearchCondition;
import teameight.gg.global.security.UserDetailsImpl;
import teameight.gg.domain.post.service.PostService;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPost(condition, pageable);
    }

    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId) {
        return postService.getSinglePost(postId);
    }

    @PostMapping("/newpost")
    public ApiResponse<?> createPost(@RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.createPost(postRequestDto, image, userDetailsImpl.getUser());
    }

    @PutMapping("/{postId}")
    public ApiResponse<?> modifyPost(@PathVariable Long postId,
                                     @RequestBody PostRequestDto postRequestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.updatePost(postId, postRequestDto, userDetailsImpl.getUser());
    }

    @DeleteMapping ("/{postId}")
    public ApiResponse<?> removePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.deletePost(postId, userDetailsImpl.getUser());
    }
}