package site.ph0en1x.task_management_sys.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.comment.CommentDTO;
import site.ph0en1x.task_management_sys.model.comment.CommentMapper;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.web.security.expression.CustomSecurityExpression;
import site.ph0en1x.task_management_sys.web.validation.onCreate;

import java.util.List;

@Tag(name = "Comment controller", description = "Comments API v1")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
@Validated
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @PostMapping("/{taskId}")
    public CommentDTO createComment(@PathVariable("taskId") Long taskId,
                                    @Validated(onCreate.class) @RequestBody CommentDTO commentDTO) {
        commentDTO.setTaskId(taskId);
        commentDTO.setUserId(CustomSecurityExpression.getCurrentUserId());

        return commentMapper.toDto(
                commentRepository.save(
                        commentMapper.toEntity(commentDTO))
        );
    }

    @GetMapping("/{taskId}")
    public List<CommentDTO> getAllComments(@PathVariable Long taskId) {
        return commentRepository.findAllByTask_Id(taskId).stream()
                .map(commentMapper::toDto)
                .toList();
    }
}
