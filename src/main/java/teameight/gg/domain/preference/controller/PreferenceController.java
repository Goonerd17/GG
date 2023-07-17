package teameight.gg.domain.preference.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teameight.gg.domain.preference.service.PreferenceService;
import teameight.gg.global.responseDto.ApiResponse;
import teameight.gg.global.security.UserDetailsImpl;


import static teameight.gg.global.utils.ResponseUtils.ok;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PreferenceController {

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
