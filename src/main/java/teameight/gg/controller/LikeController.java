package teameight.gg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teameight.gg.dto.ApiResponse;
import teameight.gg.security.UserDetailsImpl;
import teameight.gg.service.LikeService;
import teameight.gg.utils.ResponseUtils;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post/{postId}/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ApiResponse<?> clickLike(@PathVariable Long postId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ResponseUtils.ok(likeService.addLike(postId, userDetailsImpl.getUser()));
    }
}
