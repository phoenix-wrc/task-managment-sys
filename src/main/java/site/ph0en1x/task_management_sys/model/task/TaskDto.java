package site.ph0en1x.task_management_sys.model.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import site.ph0en1x.task_management_sys.model.comment.CommentDTO;
import site.ph0en1x.task_management_sys.web.validation.onCreate;
import site.ph0en1x.task_management_sys.web.validation.onUpdate;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Data
@Schema(description = "Task DTO")
public class TaskDto {

    @NotNull(message = "Id must be not null", groups = {onUpdate.class})
    @Schema(description = "Task ID", example = "3")
    private Long id;

    @NotNull(message = "Title must be not null", groups = {onCreate.class, onUpdate.class})
    @Length(max = 255, message = "Title must be less than 255 symbols", groups = {onCreate.class, onUpdate.class})
    @Schema(description = "Task title", example = "Get spiderweb")
    private String title;

    @Schema(description = "Task description", example = "Try new recipe of spiderweb")
    @Length(max = 1024, message = "Description must be less than 1024 symbols", groups = {onCreate.class, onUpdate.class})
    private String description;

    @Schema(description = "Task status can be  PENDING, IN_PROGRESS, COMPLETED",
            example = "PENDING, IN_PROGRESS, COMPLETED")
    @NotNull(message = "Status must exist", groups = {onCreate.class, onUpdate.class})
    private TaskStatus status;

    @Schema(description = "Task priority can be HIGH, MEDIUM , LOW", example = "HIGH, MEDIUM, LOW")
    @NotNull(message = "Priority must exist", groups = {onCreate.class, onUpdate.class})
    private TaskPriority priority;

    @Schema(description = "Task create date in format yyyy-MM-dd HH:mm",
            example = "2025-06-01 11:22", format = "date-time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;


    @Schema(description = "Task update date in format yyyy-MM-dd HH:mm",
            example = "2025-06-01 11:22", format = "date-time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    @Schema(description = "Author ID", example = "3")
    private Long authorId;

    @Schema(description = "Assignee user ID. Define as author id as default.", example = "3")
    private Long assigneeId;

    @Schema(description = "All comments to task")
    private Set<CommentDTO> comments;
}
