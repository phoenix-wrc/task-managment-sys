package site.ph0en1x.task_management_sys.model.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.ph0en1x.task_management_sys.model.comment.CommentMapper;
import site.ph0en1x.task_management_sys.model.exception.ResourceMappingException;
import site.ph0en1x.task_management_sys.model.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskMapper {
    private final CommentMapper commentMapper;

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
        dto.setComments(commentMapper.toDto(task.getComments()));
        return dto;
    }

    public Task toEntity(TaskDto dto) {
        if (dto == null) throw new ResourceMappingException("DTO is null");
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setUpdatedAt(LocalDateTime.now());
        User author = new User();
        author.setId(dto.getAuthorId());
        task.setAuthor(author);
        if (dto.getAssigneeId() != null) {
            User assignee = new User();
            assignee.setId(dto.getAssigneeId());
            task.setAssignee(assignee);
        } else {
            task.setAssignee(author);
        }
        task.setComments(new HashSet<>());
        return task;
    }

    public Collection<TaskDto> toDto(Collection<Task> tasks) {
        return tasks.stream().map(this::toDto).collect(Collectors.toSet());
    }
}
