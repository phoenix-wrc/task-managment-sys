package site.ph0en1x.task_management_sys.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        Task task = taskMapper.toEntity(taskDTO);
        log.debug("Create task {}", task.toString());
        return taskMapper.toDto(
                taskService.createTask(task));
    }

    @PutMapping()
    @Operation(summary = "Update task")
    @PreAuthorize("@customSecurityExpression.canAccessToTask(#task.authorId())")
    public TaskDto updateTask(@RequestBody TaskDto task) {
        log.debug("Update task {}", task.toString());
        return taskMapper.toDto(
                taskService.updateTask(
                        taskMapper.toEntity(task)));
    }

    @PutMapping()
    @Operation(summary = "Update status of task")
    @PreAuthorize("@customSecurityExpression.canAccessSetStatus(#taskDto.id())")
    public TaskDto updateTaskStatus(@RequestBody TaskDto taskDto) {
        log.debug("Update status task {}", taskDto.toString());
        Task task = taskMapper.toEntity(taskDto);

        task.setTitle(null);
        task.setDescription(null);
        task.setPriority(null);
        task.setAssignee(null);
        task.setAuthor(null);

        return taskMapper.toDto(taskService.updateTask(task));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task with id")
    @PreAuthorize("@customSecurityExpression.canAccessToTask(#id)")
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by it ID.")
    public TaskDto getTask(@PathVariable Long id) {
        log.debug("Get task with id {}", id);
        Task task = taskService.getTask(id);
        log.debug("Got task {}", task.toString());
        return taskMapper.toDto(task);
    }

    @GetMapping("/author")
    @Operation(summary = "Get all tasks by author ID, required param 'authorId' ")
    public Collection<TaskDto> getTasksByAuthor(@RequestParam Long authorId) {
        return taskMapper.toDto(
                taskService.getTasksByOwnerId(authorId)
        );
    }

    @GetMapping("/assignee")
    @Operation(summary = "Get all tasks by assignee ID, required param 'assigneeId' ")
    public Collection<TaskDto> getTasksByAssigneeId(@RequestParam Long assigneeId) {
        return taskMapper.toDto(
                taskService.getTasksByAssigneeId(assigneeId)
        );
    }

    @GetMapping
    @Operation(summary = "Get all tasks with filters, required params: \n " +
            " searchTerm - string with search line to search in title and description,\n" +
            " status - string with status, \n" +
            " priority - string with priority,\n" +
            " author - ID of author,\n" +
            " assignee - ID of assignee user." +
            "pageSize - count of task on page default is 2, " +
            "pageNumber - number of page, default is 0")
    public Page<TaskDto> getTasksWithFilter(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long author,
            @RequestParam(required = false) Long assignee,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return taskService.getTasks(
                searchTerm,
                status,
                priority,
                author,
                assignee,
                pageSize,
                pageNumber
        ).map(taskMapper::toDto);
    }
}
