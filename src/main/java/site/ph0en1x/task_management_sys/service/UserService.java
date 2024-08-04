package site.ph0en1x.task_management_sys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passEncoder;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User with " + email + "not found"));
    }

    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with " + userId + "not found")
        );
    }

    public boolean isTaskExecutor(Long userId, Long taskId) {
        Set<Task> assigned = userRepository.findById(userId).orElseThrow(() ->
                        new ResourceNotFoundException("User with ID " + userId + " not found"))
                .getAssignedTasks();
        assigned.stream().filter(task -> task.getId().equals(taskId)).collect(Collectors.toSet());

        return !assigned.isEmpty();

        //TODO Передлелать на простой SQL скрипт проверки наличия записи. Но потом. Сильно потом
    }

    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User with this username already exists");
        }
        if (!user.getPassword().equals(user.getPasswordConfirmation())) {
            throw new IllegalStateException("Password and password confirmation do not match");
        }
        user.setPassword(passEncoder.encode(user.getPassword()));
        Set<Roles> roles = Set.of(Roles.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }
}
