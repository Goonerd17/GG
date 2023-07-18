package teameight.gg.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teameight.gg.domain.user.dto.SignupRequestDto;
import teameight.gg.domain.user.entity.User;
import teameight.gg.domain.user.entity.UserRoleEnum;
import teameight.gg.domain.user.repository.UserRepository;
import teameight.gg.global.exception.InvalidConditionException;
import teameight.gg.global.responseDto.ApiResponse;

import java.util.Optional;

import static teameight.gg.global.stringCode.ErrorCodeEnum.*;
import static teameight.gg.global.stringCode.SuccessCodeEnum.*;
import static teameight.gg.global.utils.ResponseUtils.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<?> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        checkDuplicatedUsername(username);
        UserRoleEnum role = UserRoleEnum.USER;

        User user = new User(username, password, role);
        userRepository.save(user);

        return success(USER_SIGNUP_SUCCESS);
    }

    // 회원 중복 확인
    private void checkDuplicatedUsername(String username) {
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new InvalidConditionException(DUPLICATE_USERNAME_EXIST);
        }
    }
}
