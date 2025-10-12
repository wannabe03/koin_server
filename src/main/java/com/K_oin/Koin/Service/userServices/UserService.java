package com.K_oin.Koin.Service.userServices;

import com.K_oin.Koin.DTO.userDTOs.UserDTO;
import com.K_oin.Koin.DTO.userDTOs.UserUpdateProfileDTO;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import com.K_oin.Koin.EnumData.Nationality;
import com.K_oin.Koin.EnumData.Role;
import com.K_oin.Koin.Repository.userRepository.UserRepository;
import com.K_oin.Koin.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            log.warn("회원가입 실패 - 이미 존재하는 아이디: {}", userDTO.getUsername());
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            log.warn("회원가입 실패 - 이미 존재하는 이메일: {}", userDTO.getEmail());
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.findByNickname(userDTO.getNickname()).isPresent()) {
            log.warn("회원가입 실패 - 이미 존재하는 닉네임: {}", userDTO.getNickname());
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .nickname(userDTO.getNickname())
                .birthDate(userDTO.getBirthDate())
                .nationality(Nationality.valueOf(userDTO.getNationality()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        log.info("회원가입 성공 - username: {}, email: {}", saved.getUsername(), saved.getEmail());

        return saved;
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("로그인 실패 - 존재하지 않는 사용자: {}", username);
                    return new RuntimeException("사용자 없음");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("로그인 실패 - 비밀번호 불일치, username: {}", username);
            throw new RuntimeException("비밀번호 불일치");
        }

        String token = JwtUtil.createToken(username);
        log.info("로그인 성공 - username: {}", username);
        return token;
    }

    public Optional<UserDTO> getProfile(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    log.info("프로필 조회 - username: {}", username);
                    return UserDTO.builder()
                            .name(user.getName())
                            .email(user.getEmail())
                            .birthDate(user.getBirthDate())
                            .nationality(user.getNationality().name())
                            .nickname(user.getNickname())
                            .build();
                });
    }

    public Optional<Boolean> changePassword(String username, String newPassword) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    if (passwordEncoder.matches(newPassword, user.getPassword())) {
                        log.warn("비밀번호 변경 실패 - 기존 비밀번호와 동일, username: {}", username);
                        throw new IllegalArgumentException("새 비밀번호가 기존 비밀번호와 같습니다");
                    }

                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
                    log.info("비밀번호 변경 성공 - username: {}", username);
                    return true;
                });
    }

    public boolean isFieldTaken(String type, String value) {
        boolean taken = switch (type.toLowerCase()) {
            case "nickname" -> userRepository.findByNickname(value).isPresent();
            case "username" -> userRepository.findByUsername(value).isPresent();
            case "email" -> userRepository.findByEmail(value).isPresent();
            default -> throw new IllegalArgumentException("잘못된 타입입니다. username/nickname/email 중 하나여야 합니다.");
        };

        log.info("중복 체크 - type: {}, value: {}, 결과: {}", type, value, taken ? "중복" : "사용 가능");
        return taken;
    }

    public void updateProfile(UserUpdateProfileDTO userUpdateProfileDTO, String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> {
                    log.warn("프로필 수정 실패 - 존재하지 않는 사용자: {}", name);
                    return new RuntimeException("사용자 없음");
                });

        if (userUpdateProfileDTO.getNickname() != null && !userUpdateProfileDTO.getNickname().isBlank()) {
            user.setNickname(userUpdateProfileDTO.getNickname());
        }
        if (userUpdateProfileDTO.getBirthDate() != null) {
            user.setBirthDate(userUpdateProfileDTO.getBirthDate());
        }
        if (userUpdateProfileDTO.getNationality() != null) {
            user.setNationality(Nationality.valueOf(userUpdateProfileDTO.getNationality()));
        }

        userRepository.save(user);
        log.info("프로필 수정 성공 - username: {}", name);
    }
}
