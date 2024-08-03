package site.ph0en1x.task_management_sys.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.service.TaskService;

import java.util.List;

@RequiredArgsConstructor
@RestController("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping()
    public Task updateTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @GetMapping("/byAssignee/{id}")
    public List<Task> getTasksByAssigneeId(@PathVariable Long id) {
        return taskService.getTasksByAssigneeId(id);
    }

    @GetMapping("/byOwner/{id}")
    public List<Task> getTasksByOwnerId(@PathVariable Long id) {
        return taskService.getTasksByOwnerId(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}
