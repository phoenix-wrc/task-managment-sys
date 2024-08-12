package site.ph0en1x.task_management_sys.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import site.ph0en1x.TestConfig;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskPriority;
import site.ph0en1x.task_management_sys.model.task.TaskStatus;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.repository.TaskRepository;
import site.ph0en1x.task_management_sys.repository.UserRepository;
import site.ph0en1x.task_management_sys.service.impl.CommentService;
import site.ph0en1x.task_management_sys.service.impl.TaskServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static site.ph0en1x.TestUtil.getTaskWith;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private CommentService commentService;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void updateTask_withValidTask_shouldUpdateAndReturnUpdatedTask() {
        // Arrange
        Task existingTask = getTaskWith(1L, "Existing Title", "Existing Description",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, null, null);
        existingTask.setUpdatedAt(LocalDateTime.now());
        existingTask.setCreatedAt(LocalDateTime.now());

        Task updatedTask = getTaskWith(1L, "Updated Title", "Updated Description",
                TaskStatus.COMPLETED, TaskPriority.LOW, null, null);
        existingTask.setUpdatedAt(LocalDateTime.now());
        existingTask.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = taskService.updateTask(updatedTask);

        // Assert
        assertEquals(updatedTask, result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_withInvalidId_shouldThrowResourceNotFoundException() {
        // Arrange
        Task task = getTaskWith(2L, "New Title", "New Description", TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH, null, null);
        task.setUpdatedAt(LocalDateTime.now());
        task.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(task));
        verify(taskRepository, times(1)).findById(2L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_withPartialUpdates_shouldUpdateOnlySpecifiedFields() {
        // Arrange
        Task existingTask = getTaskWith(1L, "Existing Title", "Existing Description",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, null, null);
        existingTask.setUpdatedAt(LocalDateTime.now());
        existingTask.setCreatedAt(LocalDateTime.now());

        Task partialUpdate = getTaskWith(1L, "Updated Title", null,
                null, null, null, null);

        Task expectedUpdatedTask = getTaskWith(1L, "Updated Title", "Existing Description",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, null, null);
        expectedUpdatedTask.setUpdatedAt(LocalDateTime.now());
        expectedUpdatedTask.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);

        // Act
        Task result = taskService.updateTask(partialUpdate);

        // Assert
        assertEquals(expectedUpdatedTask, result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_shouldUpdateUpdatedAtField() {
        // Arrange
        LocalDateTime initialUpdatedAt = LocalDateTime.now().minusMinutes(2);

        Task existingTask = getTaskWith(1L, "Existing Title", "Existing Description",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, null, null);
        existingTask.setUpdatedAt(initialUpdatedAt);
        existingTask.setCreatedAt(LocalDateTime.now());

        Task updatedTask = getTaskWith(1L, null, null, null,
                null, null, null);
        LocalDateTime updatedUpdatedAt = LocalDateTime.now();
        updatedTask.setUpdatedAt(updatedUpdatedAt);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = taskService.updateTask(updatedTask);

        // Assert
        assertNotEquals(initialUpdatedAt, existingTask.getUpdatedAt());
        assertTrue(result.getUpdatedAt().isAfter(initialUpdatedAt));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask() {
        // 1. Подготовка тестовых данных
        Task task = getTaskWith(null, "Тестовая задача","description", TaskStatus.PENDING,
                TaskPriority.HIGH, 1L, 1L);
        Task savedTask = new Task(); // Модель, которую вернет taskRepository.save
        savedTask.setId(1L); //  Присвойте ID, если в вашем коде  save возвращает модель с id

        // 2. Ожидание вызова метода save и его поведение
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // 3. Вызов тестируемого метода
        Task result = taskService.createTask(task);

        // 4. Проверка результата
        assertThat(result).isEqualTo(savedTask);
        assertThat(result.getId()).isEqualTo(savedTask.getId());

        // 5. Проверка вызовов моков
        verify(taskRepository, times(1)).save(any(Task.class)); // Проверка, что save был вызван один раз
        verifyNoMoreInteractions(taskRepository); // Проверка, что никаких других методов не было вызвана
    }

    @Test
    void testUpdateTaskStatus() {
        // Arrange
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.COMPLETED;
        Task existingTask = getTaskWith(taskId, "Test Task","description",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH, 1L, 2L);
        Task updatedTask = getTaskWith(taskId, "Test Task", "description", newStatus,
                TaskPriority.HIGH, 1L, 2L);

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = taskService.updateTaskStatus(newStatus, taskId);

        // Assert
        assertEquals(updatedTask, result);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void testUpdateTaskStatusWithTaskNotFound() {
        // Arrange
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.COMPLETED;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty()); // Имитация отсутствия задачи

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTaskStatus(newStatus, taskId));
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testDeleteTask_TaskExists() {
        // 1. Задача существует:
        Long id = 1L;
        when(taskRepository.existsById(id)).thenReturn(true);

        // 2. Вызов метода
        taskService.deleteTask(id);

        // 3. Проверка вызова deleteById
        verify(taskRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteTask_TaskNotExists() {
        // 1. Задача не существует:
        Long id = 1L;
        when(taskRepository.existsById(id)).thenReturn(false);

        // 2. Проверка выброса исключения
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(id));

        // 3. Проверка, что deleteById не был вызван
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void getOKTask() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Task out = taskService.getTask(id);
        verify(taskRepository, times(1)).findById(id);
        assertEquals(task, out);
    }

    @Test
    void getNullTask() {
        Long id = 1L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTask(id));
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void testGetTasksWithAllParameters() {
        // Arrange
        String searchTerm = "test";
        String status = "IN_PROGRESS";
        String priority = "HIGH";
        Long author = 1L;
        Long assignee = 2L;
        int pageSize = 10;
        int pageNumber = 0;

        List<Task> tasks = Arrays.asList(
                getTaskWith(1L, "Task 1", "description",
                        TaskStatus.IN_PROGRESS, TaskPriority.HIGH, 1L, 2L),
                getTaskWith(2L, "Task 2", "description",
                        TaskStatus.IN_PROGRESS, TaskPriority.HIGH, 1L, 2L)
        );
        Page<Task> expectedPage = new PageImpl<>(tasks, PageRequest.of(pageNumber, pageSize), tasks.size());

        when(taskRepository.findAll(
                anyString(),
                any(TaskStatus.class),
                any(TaskPriority.class),
                anyLong(),
                anyLong(),
                any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Task> actualPage = taskService.getTasks(
                searchTerm,
                status,
                priority,
                author,
                assignee,
                pageSize,
                pageNumber);

        // Assert
        verify(taskRepository, times(1)).findAll(
                searchTerm,
                TaskStatus.valueOf(status),
                TaskPriority.valueOf(priority),
                author,
                assignee,
                PageRequest.of(pageNumber, pageSize));
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetTasksWithNullSearchTerm() {
        // Arrange
        String searchTerm = null;
        String status = "IN_PROGRESS";
        String priority = "HIGH";
        Long author = 1L;
        Long assignee = 2L;
        int pageSize = 10;
        int pageNumber = 0;

        List<Task> tasks = Arrays.asList(
                getTaskWith(1L, "Task 1", "description", TaskStatus.valueOf(status),
                        TaskPriority.valueOf(priority) , author, assignee),
                getTaskWith(2L, "Task 2", "description", TaskStatus.valueOf(status),
                        TaskPriority.valueOf(priority), author, assignee)
        );
        Page<Task> expectedPage = new PageImpl<>(tasks, PageRequest.of(pageNumber, pageSize), tasks.size());

        when(taskRepository.findAll(
                nullable(String.class),
                any(TaskStatus.class),
                any(TaskPriority.class),
                anyLong(),
                anyLong(),
                any(Pageable.class)))
                    .thenReturn(expectedPage);

        // Act

        Page<Task> actualPage = taskService.getTasks(
                searchTerm,
                status,
                priority,
                author,
                assignee,
                pageSize,
                pageNumber);

        // Assert
        verify(taskRepository, times(1)).findAll(
                "",
                TaskStatus.valueOf(status),
                TaskPriority.valueOf(priority),
                author,
                assignee,
                PageRequest.of(pageNumber, pageSize));
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testUpdateTaskStatus_SaveThrowsException() {
        // Arrange
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.COMPLETED;
        Task existingTask = getTaskWith(taskId,"Test Task", "description", TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH, 1L, 2L);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class)))
                .thenThrow(new RuntimeException("Save error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.updateTaskStatus(newStatus, taskId));
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void testUpdateTaskStatus_NullTaskStatus() {
        // Arrange
        Long taskId = 1L;
        TaskStatus newStatus = null;
        Task existingTask = getTaskWith(taskId, "Test Task", "description", TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH, 1L, 2L);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTaskStatus(newStatus, taskId));
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testGetTasksWithNullStatus() {
        // Arrange
        String searchTerm = "test";
        String status = null;
        String priority = "HIGH";
        Long author = 1L;
        Long assignee = 2L;
        int pageSize = 10;
        int pageNumber = 0;

        List<Task> tasks = Arrays.asList(
                getTaskWith(1L, "Task 1", "description", TaskStatus.IN_PROGRESS,
                        TaskPriority.valueOf(priority), author, assignee),
                getTaskWith(2L, "Task 2", "description", TaskStatus.IN_PROGRESS,
                        TaskPriority.valueOf(priority), author, assignee)
        );
        Page<Task> expectedPage = new PageImpl<>(tasks, PageRequest.of(pageNumber, pageSize), tasks.size());

        when(taskRepository.findAll(
                anyString(),
                nullable(TaskStatus.class),
                any(TaskPriority.class),
                anyLong(),
                anyLong(),
                any(Pageable.class)))
                    .thenReturn(expectedPage);

        // Act
        Page<Task> actualPage = taskService.getTasks(searchTerm, status, priority, author,
                assignee, pageSize, pageNumber);

        // Assert
        verify(taskRepository, times(1)).findAll(
                searchTerm,
                null,
                TaskPriority.valueOf(priority),
                author,
                assignee,
                PageRequest.of(pageNumber, pageSize));
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetTasksWithNullPriority() {
        // Arrange
        String searchTerm = "test";
        String status = "IN_PROGRESS";
        String priority = null;
        Long author = 1L;
        Long assignee = 2L;
        int pageSize = 10;
        int pageNumber = 0;

        List<Task> tasks = Arrays.asList(
                getTaskWith(1L, "Task 1", "description", TaskStatus.valueOf(status),
                        TaskPriority.HIGH, author, assignee),
                getTaskWith(2L, "Task 2", "description", TaskStatus.valueOf(status),
                        TaskPriority.HIGH, author, assignee)
        );
        Page<Task> expectedPage = new PageImpl<>(tasks, PageRequest.of(pageNumber, pageSize), tasks.size());

        when(taskRepository.findAll(
                anyString(),
                any(TaskStatus.class),
                nullable(TaskPriority.class),
                anyLong(),
                anyLong(),
                any(Pageable.class)))
                    .thenReturn(expectedPage);

        // Act
        Page<Task> actualPage = taskService.getTasks(searchTerm,
                status, priority, author, assignee, pageSize, pageNumber);

        // Assert
        verify(taskRepository, times(1)).findAll(
                searchTerm,
                TaskStatus.valueOf(status),
                null,
                author,
                assignee,
                PageRequest.of(pageNumber, pageSize));
        assertEquals(expectedPage, actualPage);
    }

}