package ua.project.testassignment.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.project.testassignment.dto.ErrorDto;
import ua.project.testassignment.exception.DateRangeException;
import ua.project.testassignment.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    private static final String OCCURRED = "occurred";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "you entered invalid input in some fields");

        List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();

        List<ErrorDto> errorDtos = new ArrayList<>(allErrors.stream()
                .map(error -> new ErrorDto(error.getField(), error.getDefaultMessage()))
                .toList());

        problemDetail.setProperty(OCCURRED, LocalDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("errors", errorDtos);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "user not found");

        problemDetail.setProperty(OCCURRED, LocalDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(DateRangeException.class)
    public ResponseEntity<ProblemDetail> handleDateRangeException(DateRangeException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid range. 'From' date should be before 'To' date.");

        problemDetail.setProperty(OCCURRED, LocalDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong. try again after a while");

        problemDetail.setProperty(OCCURRED, LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }
}
