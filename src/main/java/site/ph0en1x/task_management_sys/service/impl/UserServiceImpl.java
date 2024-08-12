package site.ph0en1x.task_management_sys.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.repository.UserRepository;
import site.ph0en1x.task_management_sys.service.UserService;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(@NonNull String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User with " + email + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(@NonNull Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with " + userId + "not found")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTaskExecutor(@NonNull Long userId, @NonNull Long taskId) {
        log.debug("Пришел запрос на проверку совпадения текущего исполнителя {} и ид в задачи {}",
                userId, taskId);
        return userRepository.existsUserByIdAndAssignedTasksId(userId, taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTaskAuthor(@NonNull Long currentUserId, @NonNull Long taskId) {
        log.debug("Пришел запрос на проверку совпадения текущего поьзователя {} и ид в задачи {}",
                currentUserId, taskId);
        return userRepository.existsUserByIdAndAuthoredTasksId(currentUserId, taskId);
    }

    @Override
    @Transactional
    public User create(@NonNull User user) {
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

    @Override
    @Transactional
    public User update(@NonNull User user) {
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
