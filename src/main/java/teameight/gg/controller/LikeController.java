package teameight.gg.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teameight.gg.dto.ApiResponse;
import teameight.gg.security.UserDetailsImpl;
import teameight.gg.service.PreferenceService;

import static teameight.gg.utils.ResponseUtils.ok;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class LikeController {

    private final PreferenceService preferenceService;

    @PostMapping("/{postId}/like")
    public ApiResponse<?> updateLike(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(preferenceService.updateLike(postId, userDetailsImpl.getUser()));
    }

    @PostMapping("/{postId}/dislike")
    public ApiResponse<?> updateDislike(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return ok(preferenceService.updateDislike(postId, userDetailsImpl.getUser()));
    }
}
