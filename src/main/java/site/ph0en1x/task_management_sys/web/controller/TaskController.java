package site.ph0en1x.task_management_sys.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskDto;
import site.ph0en1x.task_management_sys.model.task.TaskMapper;
import site.ph0en1x.task_management_sys.service.TaskService;
import site.ph0en1x.task_management_sys.web.security.expression.CustomSecurityExpression;
import site.ph0en1x.task_management_sys.web.validation.onCreate;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task controller", description = "Task API v1")
@Validated
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @Operation(summary = "Create task")
    public TaskDto createTask(@Validated(onCreate.class) @RequestBody TaskDto taskDTO) {
        taskDTO.setAuthorId(CustomSecurityExpression.getCurrentUserId());
        Task task = taskMapper.toEntity(taskDTO);
        log.debug("Create task {}", task.toString());
        return taskMapper.toDto(
                taskService.createTask(task));
    }

    @PutMapping()
    @Operation(summary = "Update task")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#task.authorId())")
    public TaskDto updateTask(@RequestBody TaskDto task) {
        return taskMapper.toDto(
                taskService.updateTask(
                        taskMapper.toEntity(task)));
    }

    @DeleteMapping("/{userId}/{id}")
    @Operation(summary = "Delete task")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public void deleteTaskById(@PathVariable Long id, @PathVariable Long userId) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task.")
    public TaskDto getTask(@PathVariable Long id) {
        return taskMapper.toDto(taskService.getTask(id));
    }

    @GetMapping
    @Operation(summary = "Get all tasks, admin only")
    @PreAuthorize("@customSecurityExpression.canAccessUser(null)")
    //Только админ может использовать, зачем то же роль админа нужна))
    public Collection<TaskDto> getAllTasks() {
        return taskMapper.toDto(taskService.getAllTasks());
    }

//         - **Фильтрация**:
    //TODO Добавить вывод с пагинацией.

    @GetMapping("/author")
    @Operation(summary = "Get all tasks by author ID, request param 'authorId' ")
    public Collection<TaskDto> getTasksByAuthor(@RequestParam Long authorId) {
        return taskMapper.toDto(
                taskService.getTasksByOwnerId(authorId)
        );
    }

    @GetMapping("/assignee")
    @Operation(summary = "Get all tasks by assignee ID, request param 'assigneeId' ")
    public Collection<TaskDto> getTasksByAssigneeId(@RequestParam Long assigneeId) {
        return taskMapper.toDto(
                taskService.getTasksByAssigneeId(assigneeId)
        );
    }
}
