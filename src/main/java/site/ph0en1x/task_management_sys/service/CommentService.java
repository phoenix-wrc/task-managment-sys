package site.ph0en1x.task_management_sys.service;

import org.springframework.data.domain.Page;
import site.ph0en1x.task_management_sys.model.comment.Comment;

public interface CommentService {
    Page<Comment> getComments(
            Long taskId,
            String searchTerm,
            Long author,
            int pageSize,
            int pageNumber);

    Comment createComment(Comment entity);
}
