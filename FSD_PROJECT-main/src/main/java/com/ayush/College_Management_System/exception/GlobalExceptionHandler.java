package com.ayush.College_Management_System.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    // Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.error("Resource not found: {}", ex.getMessage());

        return build(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Auth — User Already Exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException ex,
            HttpServletRequest request){

        log.warn("Signup conflict: {}", ex.getMessage());

        return build(
                HttpStatus.CONFLICT,
                "CONFLICT",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Auth — User Not Found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request){
        log.warn("User not found: {}", ex.getMessage());
        return build(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Auth — Bad Credentials (wrong password)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        log.warn("Bad credentials attempt at: {}", request.getRequestURI());

        return build(
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                "Invalid username or password",
                request.getRequestURI()
        );
    }

    // Auth — Access Denied (wrong role)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request){
        log.warn("Access denied at: {}", request.getRequestURI());
        return build(
                HttpStatus.FORBIDDEN,
                "FORBIDDEN",
                "You do not have permission to access this resource",
                request.getRequestURI()
        );
    }

    // Auth — Account Locked
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLocked(
            LockedException ex,
            HttpServletRequest request){

        log.warn("Locked account attempt: {}", request.getRequestURI());

        return build(
                HttpStatus.UNAUTHORIZED,
                "ACCOUNT_LOCKED",
                "Your account has been locked",
                request.getRequestURI()
        );
    }

    // Auth — Account Disabled
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(
            DisabledException ex,
            HttpServletRequest request){

        log.warn("Disabled account attempt: {}", request.getRequestURI());

        return build(
                HttpStatus.UNAUTHORIZED,
                "ACCOUNT_DISABLED",
                "Your account has been disabled",
                request.getRequestURI()
        );
    }

    // Path / query type mismatch (e.g. non-numeric {id})
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request){

        String msg = "Invalid value for parameter '" + ex.getName() + "'";
        if (ex.getRequiredType() != null) {
            msg += " (expected " + ex.getRequiredType().getSimpleName() + ")";
        }

        log.warn("Type mismatch at {}: {}", request.getRequestURI(), msg);

        return build(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                msg,
                request.getRequestURI()
        );
    }

    // Validation Errors (DTO @Valid) — ALL errors returned
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request){

        // collect ALL field errors, not just first
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.error("Validation error: {}", message);

        return build(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                message,
                request.getRequestURI()
        );
    }

    // Constraint Violation (path/query params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(
            ConstraintViolationException ex,
            HttpServletRequest request){

        log.error("Constraint violation: {}", ex.getMessage());

        return build(
                HttpStatus.BAD_REQUEST,
                "BAD_REQUEST",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Illegal state (e.g. duplicate book return)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.warn("Illegal state at {}: {}", request.getRequestURI(), ex.getMessage());

        return build(
                HttpStatus.CONFLICT,
                "CONFLICT",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // Generic Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        log.error("Internal error: ", ex);

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );
    }

    // Builder helper
    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String error,
            String message,
            String path) {

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {

        String message = "Database constraint violation";

        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("attendance")) {
            message = "Duplicate attendance entry (student + subject + date + lectureNumber)";
        }

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}