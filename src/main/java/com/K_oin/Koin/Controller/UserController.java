package com.K_oin.Koin.Controller;

import com.K_oin.Koin.DTO.ApiResponse;
import com.K_oin.Koin.DTO.userDTOs.UserChangePassWordDTO;
import com.K_oin.Koin.DTO.userDTOs.UserDTO;
import com.K_oin.Koin.DTO.userDTOs.UserUpdateProfileDTO;
import com.K_oin.Koin.Entitiy.UserEntity.User;
import com.K_oin.Koin.Service.userServices.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/management/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/Register")
    public ResponseEntity<ApiResponse<String>> RegisterUser(@RequestBody UserDTO userDTO){
        try{
            User user = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, user.getUsername(), "회원가입 성공"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 유저의 프로필 정보를 반환합니다.")
    @GetMapping("/MyProfile")
    public ResponseEntity<?> getMyProfile(
            @Parameter(description = "Spring Security 인증 객체", hidden = true)
            Authentication authentication){
        Optional<UserDTO> optionalUser = userService.getProfile(authentication.getName());

        return optionalUser
                .map(ResponseEntity::ok) // 존재하면 200 OK + DTO 반환
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body((UserDTO) Map.of(
                                "success", false,
                                "message", "user가 존재하지 않는 토큰입니다")));
    }

    @PostMapping("/ChangePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserChangePassWordDTO userChangePassWordDTO, Authentication authentication){
        try {
            Optional<Boolean> result = userService.changePassword(authentication.getName(), userChangePassWordDTO.getNewPassword());
            if (result.isPresent() && result.get()) {
                return ResponseEntity.ok(Map.of("success", true));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", "비밀번호 변경 실패"));
    }

    @GetMapping("/RegisterValidation")
    public ResponseEntity<ApiResponse<String>> checkField(
            @Parameter(description = "검사할 필드 타입 (username, nickname, email 중 하나)", example = "username", required = true)
            @RequestParam String type,
            @Parameter(description = "검사할 값 (아이디, 닉네임, 이메일)", example = "user123", required = true)
            @RequestParam String value) {

        boolean exists;

        try {
            exists = userService.isFieldTaken(type, value);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }

        String fieldName;
        switch (type.toLowerCase()) {
            case "nickname" -> fieldName = "닉네임";
            case "username" -> fieldName = "아이디";
            case "email" -> fieldName = "이메일";
            default -> throw new IllegalArgumentException("잘못된 타입입니다. username/nickname/email 중 하나여야 합니다.");
        }

        String message = exists ? "이미 존재하는 " + fieldName + "입니다."
                : "사용 가능한 " + fieldName + "입니다.";

        return ResponseEntity.ok(new ApiResponse<>(!exists, value, message));
    }

    @PostMapping("/UpdateProfile")
    public ResponseEntity<ApiResponse<UserUpdateProfileDTO>> updateProfile(@RequestBody UserUpdateProfileDTO userUpdateProfileDTO, Authentication authentication){

        try {
            userService.updateProfile(userUpdateProfileDTO, authentication.getName());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, userUpdateProfileDTO, e.getMessage()));
        }

        String message = "성공적으로" + authentication.getName() + "업데이트 하였습니다.";
        return ResponseEntity.ok(new ApiResponse<>(true, userUpdateProfileDTO, message));
    }
}
