package site.ph0en1x.task_management_sys.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.repository.UserRepository;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passEncoder;
    private final TaskService taskService;

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User with " + email + " not found"));
    }

    @Transactional(readOnly = true)
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with " + userId + "not found")
        );
    }

    @Transactional(readOnly = true)
    public boolean isTaskExecutor(Long userId, Long taskId) {
        log.debug("Пришел запрос на проверку совпадения текущего исполнителя {} и ид в задачи {}",
                userId, taskId);
        return userRepository.existsUserByIdAndAssignedTasksId(userId, taskId);
    }

    @Transactional(readOnly = true)
    public boolean isTaskAuthor(Long currentUserId, Long taskId) {
        log.debug("Пришел запрос на проверку совпадения текущего поьзователя {} и ид в задачи {}",
                currentUserId, taskId);
        return userRepository.existsUserByIdAndAuthoredTasksId(currentUserId, taskId);
    }

    @Transactional
    public User create(User user) {
        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new IllegalStateException("User with this username already exists");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match");
        }
        user.setPassword(passEncoder.encode(user.getPassword()));
        Set<Roles> roles = Set.of(Roles.ROLE_USER, Roles.ROLE_TASK_AUTHOR);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User update(User user) {
        if (!userRepository.existsUserByEmail(user.getEmail())) {
            throw new IllegalStateException("User with this username doesn't exists");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match");
        }
        user.setPassword(passEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
}
