package site.ph0en1x.task_management_sys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskPriority;
import site.ph0en1x.task_management_sys.model.task.TaskStatus;

import java.time.LocalDateTime;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE " +
            "(lower(t.title) LIKE %:searchTerm% OR " +
            "lower(t.description) LIKE %:searchTerm%) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:author IS NULL OR t.author.id = :author) " +
            "AND (:assignee IS NULL OR t.assignee.id = :assignee)")
    Page<Task> findAll(
            @Param("searchTerm") String searchTerm,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("author") Long author,
            @Param("assignee") Long assignee,
            Pageable pageable
    );

}
