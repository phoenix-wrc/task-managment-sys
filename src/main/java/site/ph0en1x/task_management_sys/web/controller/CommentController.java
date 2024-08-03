package site.ph0en1x.task_management_sys.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.comment.CommentDTO;
import site.ph0en1x.task_management_sys.model.comment.CommentMapper;
import site.ph0en1x.task_management_sys.repository.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController("/task/{taskId}/comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @PostMapping
    public CommentDTO createComment(@PathVariable("taskId") Long taskId,
                                    @RequestBody CommentDTO commentDTO) {
        commentDTO.setTaskId(taskId);
        // TODO брать текущий ИД пользователя из контекста
        return commentMapper.toDto(
                commentRepository.save(
                        commentMapper.toEntity(commentDTO))
        );
    }

    @GetMapping
    public List<CommentDTO> getAllComments(@PathVariable Long taskId) {
        return commentRepository.findAllByTask_Id(taskId).stream()
                .map(commentMapper::toDto)
                .toList();
    }
}
