package site.ph0en1x.task_management_sys.model.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
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
    @NotNull(message = "TaskId must be not null", groups = {onUpdate.class} )
    private Long taskId;

    @Schema(description = "Task's ID of", example = "3")
    private Long userId;

    @Schema(description = "Text of comment", example = "A bla-bla-bla")
    @Length(max = 1024, min = 4, message = "Text must be less than 1024 symbols and more then 4",
            groups = {onCreate.class, onUpdate.class} )
    @NotNull(message = "Text of comment must be not null", groups = {onCreate.class, onUpdate.class})
    private String text;

    @Schema(description = "Date of creating of comment in format: yyyy-MM-dd HH:mm",
            example = "2025-06-01 11:22", format = "date-time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime createdAt;
}
