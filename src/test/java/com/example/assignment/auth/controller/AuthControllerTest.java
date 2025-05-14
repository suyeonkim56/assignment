package com.example.assignment.auth.controller;

import com.example.assignment.auth.dto.response.GiveAdminResponseDto;
import com.example.assignment.auth.dto.response.RoleDto;
import com.example.assignment.auth.dto.response.SignInResponseDto;
import com.example.assignment.auth.dto.response.SignUpResponseDto;
import com.example.assignment.auth.service.AuthService;
import com.example.assignment.common.enums.UserRole;
import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean; // ✅ 교체됨
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private final String SIGNUP_JSON = """
            {
              "username": "testuser",
              "password": "1234",
              "nickname": "tester"
            }
            """;

    private final String LOGIN_JSON = """
            {
              "username": "testuser",
              "password": "1234"
            }
            """;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTests {

        @Test
        @DisplayName("정상 회원가입")
        void signupSuccess() throws Exception {
            SignUpResponseDto response = new SignUpResponseDto(
                    "testuser",
                    "tester",
                    List.of(new RoleDto(UserRole.USER))
            );

            Mockito.when(authService.signUp(any())).thenReturn(response);

            mockMvc.perform(post("/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SIGNUP_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username", is("testuser")))
                    .andExpect(jsonPath("$.nickname", is("tester")))
                    .andExpect(jsonPath("$.roles[0].role", is("USER")));
        }

        @Test
        @DisplayName("이미 가입된 사용자")
        void signupUserAlreadyExists() throws Exception {
            Mockito.when(authService.signUp(any()))
                    .thenThrow(new ClientException(ErrorCode.USER_ALREADY_EXISTS));

            mockMvc.perform(post("/signup")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(SIGNUP_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is("USER_ALREADY_EXISTS")));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class SignInTests {

        @Test
        @DisplayName("로그인 성공")
        void signinSuccess() throws Exception {
            SignInResponseDto response = new SignInResponseDto("access-token");
            Mockito.when(authService.signIn(any())).thenReturn(response);

            mockMvc.perform(post("/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(LOGIN_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token", is("access-token")));
        }

        @Test
        @DisplayName("잘못된 비밀번호")
        void signinInvalidPassword() throws Exception {
            Mockito.when(authService.signIn(any()))
                    .thenThrow(new ClientException(ErrorCode.INVALID_PASSWORD));

            mockMvc.perform(post("/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(LOGIN_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is("INVALID_PASSWORD")));
        }
    }

    @Nested
    @DisplayName("관리자 권한 부여 테스트")
    class AdminRoleTests {

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("관리자 권한 사용자 - 성공")
        void giveAdminSuccess() throws Exception {
            GiveAdminResponseDto response = new GiveAdminResponseDto(
                    "testuser",
                    "testuser",
                    List.of(new RoleDto(UserRole.ADMIN)));

            Mockito.when(authService.giveAdminRole(1L)).thenReturn(response);

            mockMvc.perform(patch("/admin/users/1/roles").with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roles[0].role", is("ADMIN")));
        }

        @Test
        @WithMockUser(roles = "USER")
        @DisplayName("일반 사용자 - 권한 오류")
        void giveAdminForbidden() throws Exception {
            mockMvc.perform(patch("/admin/users/1/roles").with(csrf()))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        @DisplayName("존재하지 않는 사용자")
        void giveAdminUserNotFound() throws Exception {
            Mockito.when(authService.giveAdminRole(99L))
                    .thenThrow(new ClientException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(patch("/admin/users/99/roles").with(csrf()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code", is("USER_NOT_FOUND")));
        }
    }
}
