package site.ph0en1x.task_management_sys.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task.setId(null);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task task) {
        if (!taskRepository.existsById(task.getId())) {
            throw new ResourceNotFoundException("Task with that ID not found");
        }
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with that ID not found");
        }
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Task getTask(Long id) {
        log.debug("Get Task: {}", id);
        return taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Задача с таким ID не найдена в базе данных"));
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByAssigneeId(Long assigneeId) {
        return taskRepository.findAllByAssigneeId(assigneeId);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByOwnerId(Long ownerId) {
        return taskRepository.findAllByAuthorId(ownerId);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

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
        return taskRepository.findAll(
                searchTerm.toLowerCase(),
                status,
                priority,
                author,
                assignee,
                pageable
        );
    }

}