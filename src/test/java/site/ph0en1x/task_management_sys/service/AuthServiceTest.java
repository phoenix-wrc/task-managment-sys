package site.ph0en1x.task_management_sys.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import site.ph0en1x.task_management_sys.model.auth.JwtRequest;
import site.ph0en1x.task_management_sys.model.auth.JwtResponse;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.repository.TaskRepository;
import site.ph0en1x.task_management_sys.repository.UserRepository;
import site.ph0en1x.task_management_sys.web.security.JwtTokenProvider;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static site.ph0en1x.TestUtil.getUserWith;

@SpringBootTest
class AuthServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @Test
    void testLogin() {
        // Arrange
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password");

        User user = getUserWith(1L,
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                loginRequest.getPassword(),
                Set.of(Roles.ROLE_USER));

        String accessToken = "AccessToken";
        String refreshToken = "RefreshToken";

        when(userService.getByEmail(loginRequest.getUsername())).thenReturn(user);
        when(tokenProvider.createAccessToken(anyLong(), anyString(), anySet()))
                .thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(anyLong(), anyString()))
                .thenReturn(refreshToken);

        JwtResponse jwtResponse = authService.login(loginRequest);

        // Assert
        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));
        assertEquals(accessToken, jwtResponse.getAccessToken());
        assertEquals(refreshToken, jwtResponse.getRefreshToken());
    }

    @Test
    void testLoginWithUserNotFound() {
        // Arrange
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setUsername("nonexistent@example.com");
        loginRequest.setPassword("password");

        when(userService.getByEmail(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }



    @Test
    void testRefresh() {
        String accessToken = "AccessToken";
        String refreshToken = "RefreshToken";
        String NewRefreshToken = "NewRefreshToken";

        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(NewRefreshToken);

        when(tokenProvider.refreshUserTokens(anyString())).thenReturn(response);

        JwtResponse jwtResponse = authService.refresh(refreshToken);

        verify(tokenProvider).refreshUserTokens(refreshToken);
        assertEquals(NewRefreshToken, jwtResponse.getRefreshToken());



    }
}