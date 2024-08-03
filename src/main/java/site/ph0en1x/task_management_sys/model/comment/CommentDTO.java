package site.ph0en1x.task_management_sys.model.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDTO {

    private Long id;

    private Long taskId;

    private Long userId;

    private String text;

    private LocalDateTime createdAt;
}
