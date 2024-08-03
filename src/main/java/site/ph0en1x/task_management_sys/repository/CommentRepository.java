package site.ph0en1x.task_management_sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ph0en1x.task_management_sys.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTask_Id(Long id);
}
