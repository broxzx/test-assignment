package ua.project.testassignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DateRangeException extends RuntimeException {

    public DateRangeException(String message) {
        super(message);
    }
}
