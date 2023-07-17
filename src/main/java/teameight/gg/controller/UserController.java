package teameight.gg.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teameight.gg.dto.ApiResponse;
import teameight.gg.dto.SignupRequestDto;
import teameight.gg.service.UserService;

import static teameight.gg.utils.ResponseUtils.*;

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
