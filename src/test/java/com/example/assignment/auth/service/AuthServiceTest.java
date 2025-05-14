package com.example.assignment.auth.service;

import com.example.assignment.auth.dto.request.SignInRequestDto;
import com.example.assignment.auth.dto.request.SignUpRequestDto;
import com.example.assignment.auth.dto.response.GiveAdminResponseDto;
import com.example.assignment.auth.dto.response.SignInResponseDto;
import com.example.assignment.auth.dto.response.SignUpResponseDto;
import com.example.assignment.auth.entity.User;
import com.example.assignment.auth.repository.AuthRepository;
import com.example.assignment.common.config.JwtUtil;
import com.example.assignment.common.config.PasswordEncoder;
import com.example.assignment.common.enums.UserRole;
import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthRepository authRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authRepository = mock(AuthRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);

        authService = new AuthService(authRepository, passwordEncoder, jwtUtil);
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTests {

        @Test
        @DisplayName("정상 회원가입")
        void signUpSuccess() {
            // given
            SignUpRequestDto dto = new SignUpRequestDto("testuser", "1234", "tester");
            when(authRepository.existsByUsername("testuser")).thenReturn(false);
            when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

            // when
            SignUpResponseDto response = authService.signUp(dto);

            // then
            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(authRepository).save(captor.capture());
            User saved = captor.getValue();

            assertThat(response.getUsername()).isEqualTo("testuser");
            assertThat(saved.getPassword()).isEqualTo("encoded1234");
            assertThat(saved.getNickname()).isEqualTo("tester");
            assertThat(saved.getUserRole()).isEqualTo(UserRole.USER);
        }

        @Test
        @DisplayName("중복 사용자 회원가입 예외")
        void signUpDuplicateUser() {
            when(authRepository.existsByUsername("testuser")).thenReturn(true);
            SignUpRequestDto dto = new SignUpRequestDto("testuser", "1234", "tester");

            assertThatThrownBy(() -> authService.signUp(dto))
                    .isInstanceOf(ClientException.class)
                    .hasMessageContaining(ErrorCode.USER_ALREADY_EXISTS.getMessage());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class SignInTests {

        @Test
        @DisplayName("로그인 성공")
        void signInSuccess() {
            SignInRequestDto dto = new SignInRequestDto("testuser", "1234");
            User user = User.of("testuser", "encoded1234", "tester");

            when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);
            when(jwtUtil.createToken(any(), eq("testuser"))).thenReturn("token-123");

            SignInResponseDto response = authService.signIn(dto);

            assertThat(response.getToken()).isEqualTo("token-123");
        }

        @Test
        @DisplayName("존재하지 않는 사용자 로그인 실패")
        void signInUserNotFound() {
            when(authRepository.findByUsername("ghost")).thenReturn(Optional.empty());

            SignInRequestDto dto = new SignInRequestDto("ghost", "1234");

            assertThatThrownBy(() -> authService.signIn(dto))
                    .isInstanceOf(ClientException.class)
                    .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("비밀번호 불일치 로그인 실패")
        void signInWrongPassword() {
            User user = User.of("testuser", "encoded1234", "tester");

            when(authRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong", "encoded1234")).thenReturn(false);

            SignInRequestDto dto = new SignInRequestDto("testuser", "wrong");

            assertThatThrownBy(() -> authService.signIn(dto))
                    .isInstanceOf(ClientException.class)
                    .hasMessageContaining(ErrorCode.INVALID_PASSWORD.getMessage());
        }
    }

    @Nested
    @DisplayName("어드민 권한 부여 테스트")
    class GiveAdminTests {

        @Test
        @DisplayName("어드민 권한 부여 성공")
        void giveAdminSuccess() {
            User user = User.of("user1", "pw", "nick");
            when(authRepository.findById(1L)).thenReturn(Optional.of(user));

            GiveAdminResponseDto response = authService.giveAdminRole(1L);

            assertThat(response.getRoles().get(0).getRole()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("없는 사용자 예외")
        void giveAdminUserNotFound() {
            when(authRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.giveAdminRole(1L))
                    .isInstanceOf(ClientException.class)
                    .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("이미 ADMIN 권한인 경우")
        void giveAdminAlreadyAdmin() {
            User user = User.of("admin", "pw", "admin");
            user.setAdmin();

            when(authRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> authService.giveAdminRole(1L))
                    .isInstanceOf(ClientException.class)
                    .hasMessageContaining(ErrorCode.ACCESS_DENIED.getMessage());
        }
    }
}
