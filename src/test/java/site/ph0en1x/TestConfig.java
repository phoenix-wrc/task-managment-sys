package site.ph0en1x;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.repository.TaskRepository;
import site.ph0en1x.task_management_sys.repository.UserRepository;
import site.ph0en1x.task_management_sys.service.impl.AuthService;
import site.ph0en1x.task_management_sys.service.impl.CommentService;
import site.ph0en1x.task_management_sys.service.impl.TaskServiceImpl;
import site.ph0en1x.task_management_sys.service.impl.UserServiceImpl;
import site.ph0en1x.task_management_sys.web.security.JwtTokenProvider;
import site.ph0en1x.task_management_sys.web.security.JwtUserDetailsService;
import site.ph0en1x.task_management_sys.web.security.props.JwtProperties;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    private final AuthenticationManager authManager;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("UHJpdmV0TW5lT2NoZW55TnV6aG5hVGhpc1JhYm90YU9jaGVu");
        return jwtProperties;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new JwtUserDetailsService(userService());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtProperties(), userDetailsService(), userService());
    }

    @Bean
    @Primary
    public UserServiceImpl userService() {
        return new UserServiceImpl(userRepository, testPasswordEncoder());
    }

    @Bean
    @Primary
    public CommentService commentService() {
        return new CommentService(commentRepository);
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService() {
        return new TaskServiceImpl(taskRepository);
    }

    @Bean
    @Primary
    public AuthService authService() {
        return new AuthService(authManager, userService(), jwtTokenProvider());
    }
}
