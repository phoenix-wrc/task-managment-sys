package site.ph0en1x.task_management_sys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.ph0en1x.task_management_sys.model.task.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAssigneeId(Long assigneeId);

    List<Task> findAllByAuthorId(Long ownerId);
}
