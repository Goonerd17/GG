package teameight.gg.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teameight.gg.dto.SignupRequestDto;
import teameight.gg.entity.User;
import teameight.gg.entity.UserRoleEnum;
import teameight.gg.repository.UserRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "ASDdsfkjfigdajdaejifalkdjfDFDAK";

    public String signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        checkDuplicatedUsername(username);
        UserRoleEnum role = getUserRoleEnum(signupRequestDto);

        User user = new User(username, password, role);
        userRepository.save(user);

        return "회원가입 성공";
    }

    // 회원 중복 확인
    private void checkDuplicatedUsername(String username) {
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            log.error("중복사용자 존재");
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
    }

    // 사용자 ROLE 확인 (관리자인 경우, ADMIN / 사옹자인 경우, USER 부여)
    private UserRoleEnum getUserRoleEnum(SignupRequestDto signupRequestDto) {
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        return role;
    }
}
