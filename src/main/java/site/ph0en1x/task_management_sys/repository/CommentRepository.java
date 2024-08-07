package site.ph0en1x.task_management_sys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.ph0en1x.task_management_sys.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTask_Id(Long id);

    @Query("SELECT c FROM Comment c WHERE " +
            "(lower(c.text) LIKE %:searchTerm%) " +
            "AND (:taskId IS NULL OR c.task.id = :taskId) " +
            "AND (:author IS NULL OR c.user.id = :author)")
    Page<Comment> findAll(
            @Param("searchTerm") String searchTerm,
            @Param("taskId") Long taskId,
            @Param("author") Long author,
            Pageable pageable
    );
}
