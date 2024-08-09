package site.ph0en1x;

import jakarta.persistence.*;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.model.task.Task;
import site.ph0en1x.task_management_sys.model.task.TaskPriority;
import site.ph0en1x.task_management_sys.model.task.TaskStatus;
import site.ph0en1x.task_management_sys.model.user.Roles;
import site.ph0en1x.task_management_sys.model.user.User;

import java.time.LocalDateTime;
import java.util.Set;

public class TestUtil {

    public static Task getTaskWith(Long id,
                             String title,
                             String description,
                             TaskStatus status,
                             TaskPriority priority,
                             Long author,
                             Long assignee) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setStatus(status);
        task.setPriority(priority);
        User user1 = new User();
        user1.setId(author);
        task.setAuthor(user1);
        User user2 = new User();
        user1.setId(assignee);
        task.setAuthor(user2);
        return task;
    }

    public static User getUserWith(long id,
                                   String email,
                                   String password,
                                   String passwordConfirmation,
                                   Set<Roles> roleUser) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        user.setRoles(roleUser);
        return user;
    }

    public static Comment getCommentWith(Long id,
                                      Long taskId,
                                         Long userId,
                                      String text) {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setText(text);
        User user = new User();
        user.setId(userId);
        comment.setUser(user);
        Task task = new Task();
        task.setId(taskId);
        comment.setTask(task);
        return comment;
    }

}
