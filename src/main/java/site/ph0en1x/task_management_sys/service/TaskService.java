package site.ph0en1x.task_management_sys.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskPriority;
import site.ph0en1x.task_management_sys.model.task.TaskStatus;
import site.ph0en1x.task_management_sys.repository.TaskRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Task createTask(@NonNull Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task.setId(null);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task task) {
        Task taskToUpdate = taskRepository.findById(task.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task with that ID not found")
        );
        if (task.getTitle() != null) taskToUpdate.setTitle(task.getTitle());
        if (task.getDescription() != null) taskToUpdate.setDescription(task.getDescription());
        if (task.getStatus() != null) taskToUpdate.setStatus(task.getStatus());
        if (task.getPriority() != null) taskToUpdate.setPriority(task.getPriority());
        if (task.getAssignee() != null) taskToUpdate.setAssignee(task.getAssignee());
        if (task.getCreatedAt() != null) taskToUpdate.setCreatedAt(task.getCreatedAt());
        if (task.getUpdatedAt() != null) taskToUpdate.setUpdatedAt(task.getUpdatedAt());

        // Почему то не работает автоматом, придется в ручную чекать и инсертить
        return taskRepository.save(taskToUpdate);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with that ID not found or not authorized");
        }
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Task getTask(Long id) {
        log.debug("Get Task: {}", id);
        return taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Задача с таким ID не найдена в базе данных"));
    }

    @Transactional
    public Task updateTaskStatus(TaskStatus taskStatus, Long id) {
        if (taskStatus == null) {
            throw new IllegalArgumentException("New status is null");
        }
        Task task = getTask(id);
        task.setStatus(taskStatus);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasks(
            String searchTerm,
            String status,
            String priority,
            Long author,
            Long assignee,
            int pageSize,
            int pageNumber
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (searchTerm == null) {
            searchTerm = "";
        }
        TaskStatus taskStatus = null;
        if (status != null) {
            status = status.toUpperCase();
            taskStatus = TaskStatus.valueOf(status);
        }
        TaskPriority taskPriority = null;
        if (priority != null) {
            priority = priority.toUpperCase();
            taskPriority = TaskPriority.valueOf(priority);
        }
        return taskRepository.findAll(
                searchTerm,
                taskStatus,
                taskPriority,
                author,
                assignee,
                pageable
        );
    }

}