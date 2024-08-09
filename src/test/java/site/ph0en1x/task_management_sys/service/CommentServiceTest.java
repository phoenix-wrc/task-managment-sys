package site.ph0en1x.task_management_sys.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import site.ph0en1x.TestConfig;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.repository.TaskRepository;
import site.ph0en1x.task_management_sys.repository.UserRepository;
import site.ph0en1x.task_management_sys.web.security.expression.CustomSecurityExpression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static site.ph0en1x.TestUtil.getCommentWith;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private TaskService taskService;

    @MockBean
    private CustomSecurityExpression customSecurityExpression;

    @Autowired
    private CommentService commentService;


    @Test
    void getComments() {
    }
    @Test
    void createComment_withValidComment_shouldSetUserIdAndSaveComment() {
        try(MockedStatic<CustomSecurityExpression> mockedStatic = Mockito.mockStatic(CustomSecurityExpression.class)) {
            // Arrange
            Long userId = 1L;
            Comment comment = getCommentWith(null, 1L, null, "Test Comment");
            mockedStatic.when(CustomSecurityExpression::getCurrentUserId).thenReturn(userId);
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);

            // Act
            Comment result = commentService.createComment(comment);

            // Assert
            assertEquals(userId, result.getUser().getId());
            verify(commentRepository, times(1)).save(comment);
        }
    }

    @Test
    void createComment_shouldSetUserIdFromSecurityContext() {

        try(MockedStatic<CustomSecurityExpression> mockedStatic = Mockito.mockStatic(CustomSecurityExpression.class)) {
            // Arrange
            Long userId = 1L;
            Comment comment = getCommentWith(null, null, null, "Test Comment");
            mockedStatic.when(CustomSecurityExpression::getCurrentUserId).thenReturn(userId);
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);

            // Act
            Comment result = commentService.createComment(comment);

            // Assert
            assertEquals(userId, result.getUser().getId());
            verify(commentRepository, times(1)).save(comment);
        }
    }

    @Test
    void createComment_shouldSaveCommentToRepository() {
        try(MockedStatic<CustomSecurityExpression> mockedStatic = Mockito.mockStatic(CustomSecurityExpression.class)) {
            // Arrange
            Long userId = 1L;

            Comment comment = getCommentWith(null, userId, null, "Test Comment");

            mockedStatic.when(CustomSecurityExpression::getCurrentUserId).thenReturn(userId);
            when(commentRepository.save(any(Comment.class))).thenReturn(comment);

            // Act
            Comment result = commentService.createComment(comment);

            // Assert
            assertEquals(comment, result);
            verify(commentRepository, times(1)).save(comment);
        }
    }

    /*

    @Test
    void testGetTasksWithAllParameters() {
        // Arrange
        String searchTerm = "test";
        String status = "IN_PROGRESS";
        String priority = "HIGH";
        Long author = 1L;
        Long assignee = 2L;
        int pageSize = 10;
        int pageNumber = 0;

        List<Task> tasks = Arrays.asList(
                getTaskWith(1L, "Task 1", "description",
                        TaskStatus.IN_PROGRESS, TaskPriority.HIGH, 1L, 2L),
                getTaskWith(2L, "Task 2", "description",
                        TaskStatus.IN_PROGRESS, TaskPriority.HIGH, 1L, 2L)
        );
        Page<Task> expectedPage = new PageImpl<>(tasks, PageRequest.of(pageNumber, pageSize), tasks.size());

        when(taskRepository.findAll(
                anyString(),
                any(TaskStatus.class),
                any(TaskPriority.class),
                anyLong(),
                anyLong(),
                any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Task> actualPage = taskService.getTasks(
                searchTerm,
                status,
                priority,
                author,
                assignee,
                pageSize,
                pageNumber);

        // Assert
        verify(taskRepository, times(1)).findAll(
                searchTerm,
                TaskStatus.valueOf(status),
                TaskPriority.valueOf(priority),
                author,
                assignee,
                PageRequest.of(pageNumber, pageSize));
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetTasksWithNullSearchTerm() {
        // Arrange
        String searchTerm = null;
        String status = "IN_PROGRESS";
        String priority = "HIGH";
        Long author = 1L;
        Long assignee = 2L;
        int pageSize = 10;
        int pageNumber = 0;

        List<Task> tasks = Arrays.asList(
                getTaskWith(1L, "Task 1", "description", TaskStatus.valueOf(status),
                        TaskPriority.valueOf(priority) , author, assignee),
                getTaskWith(2L, "Task 2", "description", TaskStatus.valueOf(status),
                        TaskPriority.valueOf(priority), author, assignee)
        );
        Page<Task> expectedPage = new PageImpl<>(tasks, PageRequest.of(pageNumber, pageSize), tasks.size());

        when(taskRepository.findAll(
                nullable(String.class),
                any(TaskStatus.class),
                any(TaskPriority.class),
                anyLong(),
                anyLong(),
                any(Pageable.class)))
                    .thenReturn(expectedPage);

        // Act

        Page<Task> actualPage = taskService.getTasks(
                searchTerm,
                status,
                priority,
                author,
                assignee,
                pageSize,
                pageNumber);

        // Assert
        verify(taskRepository, times(1)).findAll(
                "",
                TaskStatus.valueOf(status),
                TaskPriority.valueOf(priority),
                author,
                assignee,
                PageRequest.of(pageNumber, pageSize));
        assertEquals(expectedPage, actualPage);
    }


     */
}