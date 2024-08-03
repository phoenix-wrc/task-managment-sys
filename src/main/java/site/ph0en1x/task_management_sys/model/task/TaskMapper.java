package site.ph0en1x.task_management_sys.model.task;

import org.springframework.stereotype.Component;
import site.ph0en1x.task_management_sys.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setAuthorId(task.getAuthor().getId());
        dto.setAssigneeId(task.getAssignee().getId());
        return dto;
    }

    public Task toEntity(TaskDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setUpdatedAt(dto.getUpdatedAt());
        User author = new User(); // TODO заменить на взятие ИД из контекста, за других создавать таски нельзя
        author.setId(dto.getAuthorId());
        task.setAuthor(author);
        User assignee = new User();
        assignee.setId(dto.getAssigneeId());
        task.setAssignee(assignee);
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }

    public List<TaskDto> toDto(List<Task> tasks) {
        return tasks.stream().map(this::toDto).toList() ;

//        List<TaskDto> tasksDto = new ArrayList<>();
//
//        for (Task task : tasks) {
//            tasksDto.add(toDto(task));
//        }
//
//        return tasksDto;
    }
}
