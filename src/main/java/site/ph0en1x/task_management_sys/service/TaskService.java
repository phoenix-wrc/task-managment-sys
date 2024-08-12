package site.ph0en1x.task_management_sys.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskStatus;

public interface TaskService {
    @Transactional
    Task createTask(@NonNull Task task);

    @Transactional
    Task updateTask(Task task);

    @Transactional
    void deleteTask(Long id);

    @Transactional(readOnly = true)
    Task getTask(Long id);

    @Transactional
    Task updateTaskStatus(TaskStatus taskStatus, Long id);

    @Transactional(readOnly = true)
    Page<Task> getTasks(
            String searchTerm,
            String status,
            String priority,
            Long author,
            Long assignee,
            int pageSize,
            int pageNumber
    );
}
