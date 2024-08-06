package site.ph0en1x.task_management_sys.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import site.ph0en1x.task_management_sys.model.exception.ResourceMappingException;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task.setId(null);
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        if (!taskRepository.existsById(task.getId())) {
            throw new ResourceNotFoundException("Task with that ID not found");
        }
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with that ID not found");
        }
        taskRepository.deleteById(id);
    }

    public Task getTask(Long id) {
        log.debug("Get Task: {}", id);
        return taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Задача с таким ID не найдена в базе данных"));
    }

    public List<Task> getTasksByAssigneeId(Long assigneeId) {
        return taskRepository.findAllByAssigneeId(assigneeId);
    }

    public List<Task> getTasksByOwnerId(Long ownerId) {
        return taskRepository.findAllByAuthorId(ownerId);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}