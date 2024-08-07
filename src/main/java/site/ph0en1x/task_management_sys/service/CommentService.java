package site.ph0en1x.task_management_sys.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.repository.CommentRepository;

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
        return commentRepository.findAll(
                searchTerm.toLowerCase(),
                taskId,
                author,
                pageable
        );
    }

    public Comment createComment(Comment entity) {
        return commentRepository.save(entity);
    }
}
