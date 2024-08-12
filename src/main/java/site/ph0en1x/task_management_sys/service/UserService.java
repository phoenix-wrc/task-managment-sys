package site.ph0en1x.task_management_sys.service;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import site.ph0en1x.task_management_sys.model.user.User;

public interface UserService {
    @Transactional(readOnly = true)
    User getByEmail(@NonNull String email);

    @Transactional(readOnly = true)
    User getById(@NonNull Long userId);

    @Transactional(readOnly = true)
    boolean isTaskExecutor(@NonNull Long userId, @NonNull Long taskId);

    @Transactional(readOnly = true)
    boolean isTaskAuthor(@NonNull Long currentUserId, @NonNull Long taskId);

    @Transactional
    User create(@NonNull User user);

    @Transactional
    User update(@NonNull User user);
}
