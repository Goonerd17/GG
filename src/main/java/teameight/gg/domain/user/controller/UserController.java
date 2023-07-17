package teameight.gg.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teameight.gg.domain.user.dto.SignupRequestDto;
import teameight.gg.domain.user.service.UserService;
import teameight.gg.global.responseDto.ApiResponse;


import static teameight.gg.global.utils.ResponseUtils.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return ok(userService.signup(signupRequestDto));
    }
}
