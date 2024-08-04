package site.ph0en1x.task_management_sys.model.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import site.ph0en1x.task_management_sys.web.validation.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Comment DTO")
public class CommentDTO {

    @Schema(description = "Comment's ID", example = "3")
    @NotNull(message = "Id must be not null", groups = onUpdate.class)
    private Long id;

    @Schema(description = "Task's ID of", example = "3")
    @NotNull(message = "TaskId must be not null", groups = {onCreate.class, onUpdate.class} )
    private Long taskId;

    @Schema(description = "Task's ID of", example = "3")
    @NotNull(message = "UserId must be not null", groups = {onCreate.class, onUpdate.class} )
    private Long userId;

    @Schema(description = "Text of comment", example = "A bla-bla-bla")
    @Length(max = 1024, message = "Text must be less than 1024 symbols", groups = {onCreate.class, onUpdate.class} )
    private String text;

    @Schema(description = "User's name", example = "Ronald")
    @NotNull(message = "Name must be not null", groups = {onCreate.class, onUpdate.class})
    @Length(max = 255, message = "Name must be less than 255 symbols", groups = {onCreate.class, onUpdate.class} )
    private LocalDateTime createdAt;
}
