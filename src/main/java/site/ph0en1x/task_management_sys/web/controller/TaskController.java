package site.ph0en1x.task_management_sys.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskDto;
import site.ph0en1x.task_management_sys.model.task.TaskMapper;
import site.ph0en1x.task_management_sys.service.TaskService;

import java.util.List;

@RequiredArgsConstructor
@RestController("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    public TaskDto createTask(@RequestBody TaskDto taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        return taskMapper.toDto(
                taskService.createTask(task));
    }

    @PutMapping()
    public TaskDto updateTask(@RequestBody TaskDto task) {
        return taskMapper.toDto(
                taskService.updateTask(
                        taskMapper.toEntity(task)));
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
    public List<TaskDto> getTasksByAssigneeId(@PathVariable Long id) {
        return taskMapper.toDto(taskService.getTasksByAssigneeId(id));
    }

    @GetMapping("/byOwner/{id}")
    public List<TaskDto> getTasksByOwnerId(@PathVariable Long id) {
        return taskMapper.toDto(taskService.getTasksByOwnerId(id));
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskMapper.toDto(taskService.getAllTasks());
    }
}
