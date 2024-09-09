package ru.practicum.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.exceptions.ConflictException;
import ru.practicum.exception.exceptions.NotFoundException;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError missingParameterHandle(final MissingServletRequestParameterException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError missingParameterHandle(final IllegalArgumentException e) {
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
    public ApiError exceedHandle(final ConflictException e) {
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
                .message(e.getClass().getSimpleName())
                .time(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER))
                .build();
    }
}
