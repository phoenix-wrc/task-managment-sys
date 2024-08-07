package site.ph0en1x.task_management_sys.model.comment;

import org.springframework.stereotype.Component;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.user.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDTO toDto(Comment entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setTaskId(entity.getTask().getId());
        dto.setUserId(entity.getUser().getId());
        return dto;
    }

    public Comment toEntity(CommentDTO dto) {
        Comment entity = new Comment();
        entity.setText(dto.getText());
        Task task = new Task();
        task.setId(dto.getTaskId());
        entity.setTask(task);
        User user = new User();
        user.setId(dto.getUserId());
        entity.setUser(user);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public Set<CommentDTO> toDto(Set<Comment> comments) {
        return comments.stream().map(this::toDto).collect(Collectors.toSet());

    }
}
