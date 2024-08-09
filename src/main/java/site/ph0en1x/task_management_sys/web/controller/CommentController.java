package site.ph0en1x.task_management_sys.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.comment.CommentDTO;
import site.ph0en1x.task_management_sys.model.comment.CommentMapper;
import site.ph0en1x.task_management_sys.service.CommentService;
import site.ph0en1x.task_management_sys.web.security.expression.CustomSecurityExpression;
import site.ph0en1x.task_management_sys.web.validation.onCreate;

@Tag(name = "Comment controller", description = "Comments API v1")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
@Validated
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping("/{taskId}")
    @Operation(summary = "Create an comment to task.")
    public CommentDTO createComment(@PathVariable("taskId") Long taskId,
                                    @Validated(onCreate.class) @RequestBody CommentDTO commentDTO) {

        commentDTO.setTaskId(taskId);
        return commentMapper.toDto(
                commentService.createComment(
                        commentMapper.toEntity(commentDTO))
        );
    }

    @GetMapping("/{taskId}")
    @Operation(summary = """
            Get all task's comments with filters.
            Not required params:
            searchTerm - string with search line to search in text,
            author - ID of author.
            Required params:
            pageSize - count of task on page default is 2,
            pageNumber - number of page, default is 0.""")
    public Page<CommentDTO> getComments(
            @PathVariable Long taskId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Long author,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return commentService.getComments(
                taskId,
                searchTerm,
                author,
                pageSize,
                pageNumber
        ).map(commentMapper::toDto);
    }
}
