package ru.practicum.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.exceptions.NotFoundException;
import ru.practicum.exception.exceptions.ParticipentLimitException;
import ru.practicum.exception.exceptions.ParticipentsLimitExceedException;
import ru.practicum.exception.exceptions.StateException;
import ru.practicum.utils.Constants;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionHandlerContoller {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundHandle(final NotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validationHandle(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getFieldError().getDefaultMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError participentHandle(final ParticipentLimitException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError stateHandle(final StateException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError violationsHandle(final DataIntegrityViolationException e) {
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            System.out.println(stackTraceElement.toString());
        }
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError exceedHandle(final ParticipentsLimitExceedException e) {
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            System.out.println(stackTraceElement.toString());
        }
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Limit exceed.")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError throwableHandle(final Throwable e) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("(--)")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }
}
