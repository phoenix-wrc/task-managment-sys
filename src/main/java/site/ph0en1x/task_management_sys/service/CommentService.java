package site.ph0en1x.task_management_sys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.web.security.expression.CustomSecurityExpression;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Page<Comment> getComments(
            Long taskId,
            String searchTerm,
            Long author,
            int pageSize,
            int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if(searchTerm == null ) {
            searchTerm = "";
        }
        return commentRepository.findAll(
                searchTerm,
                taskId,
                author,
                pageable
        );
    }

    public Comment createComment(Comment entity) {
        entity.getUser().setId(CustomSecurityExpression.getCurrentUserId());
        return commentRepository.save(entity);
    }
}
