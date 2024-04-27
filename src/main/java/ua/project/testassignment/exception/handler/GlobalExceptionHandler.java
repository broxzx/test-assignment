package ua.project.testassignment.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "you entered invalid input in some fields");

        List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();

        List<ErrorDto> errorDtos = new ArrayList<>(allErrors.stream()
                .map(error -> new ErrorDto(error.getField(), error.getDefaultMessage()))
                .toList());

        problemDetail.setProperty("occurred", LocalDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("errors", errorDtos);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "user not found");

        problemDetail.setProperty("occurred", LocalDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(DateRangeException.class)
    public ResponseEntity<ProblemDetail> handleDateRangeException(DateRangeException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid range. 'From' date should be before 'To' date.");

        problemDetail.setProperty("occurred", LocalDateTime.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

}
