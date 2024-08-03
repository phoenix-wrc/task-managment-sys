package site.ph0en1x.task_management_sys.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.repository.TaskRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task getTask(Long id) {
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