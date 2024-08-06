package site.ph0en1x.task_management_sys.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import site.ph0en1x.task_management_sys.model.auth.JwtRequest;
import site.ph0en1x.task_management_sys.model.auth.JwtResponse;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.web.security.JwtTokenProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public JwtResponse login(JwtRequest loginRequest) {

        JwtResponse jwtResponse = new JwtResponse();
        log.debug("Get JwtRequest loginRequest with login:{} and password: {}",
                loginRequest.getUsername(), loginRequest.getPassword());

        var token = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        log.debug("UsernamePasswordAuthenticationToken create; with login:{} and password: {}",
                token.getName(), token.getCredentials());

        authenticationManager.authenticate(token);
        User user = userService.getByEmail(loginRequest.getUsername());
        log.debug("Get user with username:{} and id: {}",
                user.getEmail(), user.getId());

        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getEmail());
        jwtResponse.setAccessToken(tokenProvider.createAccessToken(user.getId(), user.getEmail(),
                new HashSet<>(user.getRoles())));
        jwtResponse.setRefreshToken(tokenProvider.createRefreshToken(user.getId(), user.getEmail()));
        return jwtResponse;
    }

    public JwtResponse refresh(String refreshToken) {
        return tokenProvider.refreshUserTokens(refreshToken);
    }
}
