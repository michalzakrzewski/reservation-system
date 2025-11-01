package com.zakrzewski.reservationsystem.exceptions.handler;

import com.zakrzewski.reservationsystem.exceptions.*;
import com.zakrzewski.reservationsystem.exceptions.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AppExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatusCode status,
                                                                  final WebRequest request) {
        final String errorMessage = "Field validation: " + ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " - " + e.getDefaultMessage())
                .findFirst()
                .orElse("Multiple fields");

        LOG.warn("Problem with validation: {}", errorMessage);
        final ErrorResponse errorBody = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorBody, headers, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status) {
        final ErrorResponse error = new ErrorResponse(message);
        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(RateLimitExceededException ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        LOG.error("Unhandled exception 500 - INTERNAL_SERVER_ERROR", ex);
        return createErrorResponse("Unhandled exception", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
