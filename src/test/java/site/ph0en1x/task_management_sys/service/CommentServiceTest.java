package site.ph0en1x.task_management_sys.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import site.ph0en1x.TestConfig;
import site.ph0en1x.task_management_sys.model.comment.Comment;
import site.ph0en1x.task_management_sys.repository.CommentRepository;
import site.ph0en1x.task_management_sys.repository.TaskRepository;
import site.ph0en1x.task_management_sys.repository.UserRepository;
import site.ph0en1x.task_management_sys.service.impl.CommentService;
import site.ph0en1x.task_management_sys.service.impl.TaskServiceImpl;
import site.ph0en1x.task_management_sys.web.security.expression.CustomSecurityExpression;

import java.util.ArrayList;
import java.util.List;

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
    private TaskServiceImpl taskService;

    @MockBean
    private CustomSecurityExpression customSecurityExpression;

    @Autowired
    private CommentService commentService;


    @Test
    void getComments() {
    }

    @Test
    void createComment_withValidComment_shouldSetUserIdAndSaveComment() {
        try (MockedStatic<CustomSecurityExpression> mockedStatic = Mockito.mockStatic(CustomSecurityExpression.class)) {
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

        try (MockedStatic<CustomSecurityExpression> mockedStatic = Mockito.mockStatic(CustomSecurityExpression.class)) {
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
        try (MockedStatic<CustomSecurityExpression> mockedStatic = Mockito.mockStatic(CustomSecurityExpression.class)) {
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

    //GetComment tests

    @Test
    void getComments_withValidParameters_shouldReturnCommentsPage() {
        // Arrange
        Long taskId = 1L;
        String searchTerm = "test";
        Long author = null; // null means no author filter
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Comment> comments = new ArrayList<>();
        comments.add(getCommentWith(1L, taskId, 1L, "Test Comment 1"));
        comments.add(getCommentWith(2L, taskId, 2L, "Test Comment 2"));
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAll(searchTerm, taskId, author, pageable)).thenReturn(expectedPage);

        // Act
        Page<Comment> result = commentService.getComments(taskId, searchTerm, author, pageSize, pageNumber);

        // Assert
        assertEquals(expectedPage, result);
        verify(commentRepository, times(1)).findAll(searchTerm, taskId, author, pageable);
    }

    @Test
    void getComments_withNullSearchTerm_shouldUseEmptySearchTerm() {
        // Arrange
        Long taskId = 1L;
        String searchTerm = null;
        Long author = null; // null means no author filter
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Comment> comments = new ArrayList<>();
        comments.add(getCommentWith(1L, taskId, 1L, "Test Comment 1"));
        comments.add(getCommentWith(2L, taskId, 2L, "Test Comment 2"));
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAll("", taskId, author, pageable)).thenReturn(expectedPage);

        // Act
        Page<Comment> result = commentService.getComments(taskId, searchTerm, author, pageSize, pageNumber);

        // Assert
        assertEquals(expectedPage, result);
        verify(commentRepository, times(1)).findAll("", taskId, author, pageable);
    }

    @Test
    void getComments_withAuthorFilter_shouldApplyAuthorFilter() {
        // Arrange
        Long taskId = 1L;
        String searchTerm = "";
        Long author = 1L;
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Comment> comments = new ArrayList<>();
        comments.add(getCommentWith(1L, taskId, author, "Test Comment 1"));
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAll(searchTerm, taskId, author, pageable)).thenReturn(expectedPage);

        // Act
        Page<Comment> result = commentService.getComments(taskId, searchTerm, author, pageSize, pageNumber);

        // Assert
        assertEquals(expectedPage, result);
        verify(commentRepository, times(1)).findAll(searchTerm, taskId, author, pageable);
    }


    @Test
    void getComments_withEmptySearchTerm_shouldReturnAllComments() {
        // Arrange
        Long taskId = 1L;
        String searchTerm = "";
        Long author = null; // null means no author filter
        int pageSize = 10;
        int pageNumber = 0;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Comment> comments = new ArrayList<>();
        comments.add(getCommentWith(1L, taskId, 1L, "Test Comment 1"));
        comments.add(getCommentWith(2L, taskId, 2L, "Test Comment 2"));
        Page<Comment> expectedPage = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAll(searchTerm, taskId, author, pageable)).thenReturn(expectedPage);

        // Act
        Page<Comment> result = commentService.getComments(taskId, searchTerm, author, pageSize, pageNumber);

        // Assert
        assertEquals(expectedPage, result);
        verify(commentRepository, times(1)).findAll(searchTerm, taskId, author, pageable);
    }
}