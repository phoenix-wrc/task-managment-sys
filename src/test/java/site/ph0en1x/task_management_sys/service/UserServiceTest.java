package site.ph0en1x.task_management_sys.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import site.ph0en1x.TestConfig;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.repository.TaskRepository;
import site.ph0en1x.task_management_sys.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskService taskService;

    @MockBean
    private BCryptPasswordEncoder passEncoder;

    @Autowired
    private UserService userService;


    @Test
    void testGetByEmail_UserExists() {
        // 1. Пользователь с заданным email существует
        String email = "test@example.com";
        User user = new User(); // Создайте тестовую модель пользователя
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // 2. Вызов метода
        User result = userService.getByEmail(email);

        // 3. Проверка результата
        assertEquals(result, user);
        verify(userRepository, times(1))
                .findByEmail(email);
    }

    @Test
    void testGetByEmail_UserNotExists() {
        // 1. Пользователь с заданным email не существует
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // 2. Проверка выброса исключения
        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail(email));
        verify(userRepository, times(1))
                .findByEmail(email);
    }

    @Test
    void testGetByEmail_EmptyEmail() {
        // 1. Пустой email
        String email = "";

        // 2. Проверка выброса исключения
        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    void testGetById_UserExists() {
        // 1. Пользователь с заданным ID существует
        Long userId = 1L;
        User user = new User(); // Создайте тестовую модель пользователя
        user.setId(userId);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // 2. Вызов метода
        User result = userService.getById(userId);

        // 3. Проверка результата
        assertEquals(result, user);
        verify(userRepository, times(1))
                .findById(userId);
    }

    @Test
    void testGetById_UserNotExists() {
        // 1. Пользователь с заданным ID не существует
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // 2. Проверка выброса исключения
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(userId));
        verify(userRepository, times(1))
                .findById(userId);
    }

    @Test
    void testGetById_NullId() {
        // 1. ID равен null
        Long userId = null;

        // 2. Проверка выброса исключения
        assertThrows(NullPointerException.class, () -> userService.getById(userId));

        // 3. Проверка, что findById не был вызван (необязательно, но желательно)
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testGetById_NegativeId() {
        // 1. ID отрицательное
        Long userId = -1L;

        // 2. Проверка выброса исключения
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(userId));

        // 3. Проверка, что findById был вызван с отрицательным ID
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testIsTaskExecutor_UserIsExecutor() {
        // 1. Пользователь является исполнителем задачи
        Long userId = 1L;
        Long taskId = 2L;
        when(userRepository.existsUserByIdAndAssignedTasksId(userId, taskId)).thenReturn(true);

        // 2. Вызов метода
        boolean result = userService.isTaskExecutor(userId, taskId);

        // 3. Проверка результата
        assertTrue(result);
        verify(userRepository, times(1))
                .existsUserByIdAndAssignedTasksId(userId, taskId);
    }

    @Test
    void testIsTaskExecutor_UserIsNotExecutor() {
        // 1. Пользователь не является исполнителем задачи
        Long userId = 1L;
        Long taskId = 2L;
        when(userRepository.existsUserByIdAndAssignedTasksId(userId, taskId)).thenReturn(false);

        // 2. Вызов метода
        boolean result = userService.isTaskExecutor(userId, taskId);

        // 3. Проверка результата
        assertFalse(result);
        verify(userRepository, times(1))
                .existsUserByIdAndAssignedTasksId(userId, taskId);
    }

    @Test
    void testIsTaskExecutor_NullUserId() {
        // 1. userId равен null
        Long userId = null;
        Long taskId = 2L;

        // 2. Проверка, что метод выкинет исключение
        assertThrows(NullPointerException.class, () -> userService.isTaskExecutor(userId, taskId));

        // 3. Проверка, что userRepository.existsUserByIdAndAssignedTasksId не был вызван
        verify(userRepository, never()).existsUserByIdAndAssignedTasksId(anyLong(), anyLong());
    }

    @Test
    void testIsTaskExecutor_NullTaskId() {
        // 1. taskId равен null
        Long userId = 1L;
        Long taskId = null;

        // 2. Проверка, что метод не выкинет исключение
        assertThrows(NullPointerException.class, () -> userService.isTaskExecutor(userId, taskId));

        // 3. Проверка, что userRepository.existsUserByIdAndAssignedTasksId не был вызван
        verify(userRepository, never()).existsUserByIdAndAssignedTasksId(anyLong(), anyLong());
    }

    @Test
    void testIsTaskAuthor_UserIsAuthor() {
        // 1. Пользователь является автором задачи
        Long currentUserId = 1L;
        Long taskId = 2L;
        when(userRepository.existsUserByIdAndAuthoredTasksId(currentUserId, taskId)).thenReturn(true);

        // 2. Вызов метода
        boolean result = userService.isTaskAuthor(currentUserId, taskId);

        // 3. Проверка результата

        verify(userRepository, times(1))
                .existsUserByIdAndAuthoredTasksId(anyLong(), anyLong());
        assertTrue(result);
    }

    @Test
    void testIsTaskAuthor_UserIsNotAuthor() {
        // 1. Пользователь не является автором задачи
        Long currentUserId = 1L;
        Long taskId = 2L;
        when(userRepository.existsUserByIdAndAuthoredTasksId(currentUserId, taskId)).thenReturn(false);

        // 2. Вызов метода
        boolean result = userService.isTaskAuthor(currentUserId, taskId);

        // 3. Проверка результата
        assertFalse(result);
        verify(userRepository, times(1))
                .existsUserByIdAndAuthoredTasksId(anyLong(), anyLong());
    }

    @Test
    void testIsTaskAuthor_NullCurrentUserId() {
        // 1. currentUserId равен null
        Long currentUserId = null;
        Long taskId = 2L;

        // 2. Проверка выброса исключения
        assertThrows(NullPointerException.class, () -> userService.isTaskAuthor(currentUserId, taskId));

        // 3. Проверка, что existsUserByIdAndAuthoredTasksId не был вызван
        verify(userRepository, never())
                .existsUserByIdAndAuthoredTasksId(anyLong(), anyLong());
    }

    @Test
    void testIsTaskAuthor_NullTaskId() {
        // 1. taskId равен null
        Long currentUserId = 1L;
        Long taskId = null;

        // 2. Проверка выброса исключения
        assertThrows(NullPointerException.class, () -> userService.isTaskAuthor(currentUserId, taskId));

        // 3. Проверка, что existsUserByIdAndAuthoredTasksId не был вызван
        verify(userRepository, never()).existsUserByIdAndAuthoredTasksId(anyLong(), anyLong());
    }

    @Test
    void testCreate_ValidUser() {
        // 1. Подготовка тестовых данных
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPasswordConfirmation("password"); // Пароли совпадают
        String encodedPassword = "encodedPassword"; // Зашифрованный пароль
        when(passEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(false); // Пользователь с таким email не существует

        // 2. Вызов метода
        User createdUser = userService.create(user);

        // 3. Проверка результата
        assertEquals(createdUser.getPassword(), encodedPassword); // Проверка шифрования
        assertIterableEquals(createdUser.getRoles(), Set.of(Roles.ROLE_USER, Roles.ROLE_TASK_AUTHOR)); // Проверка присвоения ролей
        verify(userRepository, times(1)).save(any(User.class)); // Проверка вызова save
    }

    @Test
    void testCreate_UserAlreadyExists() {
        // 1. Подготовка тестовых данных
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPasswordConfirmation("password"); // Пароли совпадают
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true); // Пользователь с таким email уже существует

        // 2. Проверка выброса исключения
        assertThrows(IllegalStateException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any(User.class)); // Проверка, что save не был вызван
    }

    @Test
    void testCreate_PasswordsDoNotMatch() {
        // 1. Подготовка тестовых данных
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPasswordConfirmation("wrongpassword"); // Пароли не совпадают
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(false); // Пользователь с таким email не существует

        // 2. Проверка выброса исключения
        assertThrows(IllegalStateException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any(User.class)); // Проверка, что save не был вызван
    }


    @Test
    void testUpdate_ValidUser() {
        // 1. Подготовка тестовых данных
        User user = new User();
        user.setId(1L); // Установите ID для пользователя
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPasswordConfirmation("password"); // Пароли совпадают
        String encodedPassword = "encodedPassword"; // Зашифрованный пароль
        when(passEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true); // Пользователь с таким email существует

        // 2. Вызов метода
        User updatedUser = userService.update(user);

        // 3. Проверка результата
        assertEquals(updatedUser.getPassword(), encodedPassword); // Проверка шифрования
        verify(userRepository, times(1)).save(any(User.class)); // Проверка вызова save
    }

    @Test
    void testUpdate_UserNotExists() {
        // 1. Подготовка тестовых данных
        User user = new User();
        user.setId(1L); // Установите ID для пользователя
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPasswordConfirmation("password"); // Пароли совпадают
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(false); // Пользователь с таким email не существует

        // 2. Проверка выброса исключения
        assertThrows(IllegalStateException.class, () -> userService.update(user));
        verify(userRepository, never()).save(any(User.class)); // Проверка, что save не был вызван
    }

    @Test
    void testUpdate_PasswordsDoNotMatch() {
        // 1. Подготовка тестовых данных
        User user = new User();
        user.setId(1L); // Установите ID для пользователя
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setPasswordConfirmation("wrongpassword"); // Пароли не совпадают
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true); // Пользователь с таким email существует

        // 2. Проверка выброса исключения
        assertThrows(IllegalStateException.class, () -> userService.update(user));
        verify(userRepository, never()).save(any(User.class)); // Проверка, что save не был вызван
    }
}