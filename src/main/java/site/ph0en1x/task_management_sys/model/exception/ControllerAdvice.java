package site.ph0en1x.task_management_sys.model.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import site.ph0en1x.task_management_sys.model.task.TaskPriority;
import site.ph0en1x.task_management_sys.model.task.TaskStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(site.ph0en1x.task_management_sys.model.exception.ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handlerResourceNotFound(ResourceNotFoundException ex) {
        return new ExceptionBody(ex.getMessage());
    }

    @ExceptionHandler(site.ph0en1x.task_management_sys.model.exception.ResourceMappingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerResourceMappingException(ResourceMappingException ex) {
        return new ExceptionBody(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerIllegalStateException(IllegalStateException ex) {
        return new ExceptionBody(ex.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerAccessDeniedException(AccessDeniedException ex) {
        log.debug("handlerAccessDeniedException {}", ex.getMessage());
        return new ExceptionBody("Access denied");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var body = new ExceptionBody("Validation failed");
        List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : errorList) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        body.setErrors(errorMap);

        return body;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerConstraintViolationException(ConstraintViolationException ex) {
        var body = new ExceptionBody("Validation failed");
        body.setErrors(ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        return body;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerAuthenticationException(AuthenticationException ex) {
        log.debug("handlerAuthenticationException {}", ex.getMessage());
        return new ExceptionBody("Authentication failed. " + ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerExpiredJwtException(ExpiredJwtException ex) {
        log.debug("handlerExpiredJwtException {}", ex.getMessage());
        return new ExceptionBody("User's token has expired. Please refresh token");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerDataIntegrityViolationException(Exception exception) {
        log.debug("{} {}", exception.getMessage(), exception.getClass().getName());
        if (exception.getMessage().contains(
                "ERROR: insert or update on table \"tasks\" violates foreign key constraint")) {
            return new ExceptionBody("There is no user with one of specified ID in the database");
        }
        return new ExceptionBody("Unknown Data Integrity error.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerIllegalArgumentException(IllegalArgumentException exception) {
        log.debug("{} {}", exception.getMessage(), exception.getClass().getName());
        if (exception.getMessage().contains(
                "No enum constant site.ph0en1x.task_management_sys.model.task.TaskPriority")) {
            return new ExceptionBody("There is no TaskPriority with specified text. " +
                    "Task priority could be: " + Arrays.toString(TaskPriority.values()));
        }
        if (exception.getMessage().contains(
                "No enum constant site.ph0en1x.task_management_sys.model.task.TaskStatus")) {
            return new ExceptionBody("There is no TaskStatus with specified text. " +
                    "Task priority could be: " + Arrays.toString(TaskStatus.values()));
        }
        return new ExceptionBody("Not correct parameter. Check your parameters.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerException(MethodArgumentTypeMismatchException exception) {
        log.debug("Into parameter inserted string, when need Number");
        return new ExceptionBody("Invalid parameter inserted. Please insert another type of parameter");
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ExceptionBody handlerException(Exception exception) {
//        log.debug("{} with message {} and with stacktrace \n {}",
//                exception.getClass().getName(), exception.getMessage(), Arrays.toString(exception.getStackTrace()));
//        return new ExceptionBody("Internal error. Please try again later.");
//    }
}
