package com.kazuki43zoo.api.error;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.ExceptionCodeResolver;
import org.terasoluna.gfw.common.exception.ResourceNotFoundException;
import org.terasoluna.gfw.common.exception.ResultMessagesNotificationException;

import javax.inject.Inject;

@ControllerAdvice
public class ApiGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Inject
    ApiErrorCreator apiErrorCreator;

    @Inject
    ExceptionCodeResolver exceptionCodeResolver;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex, final Object body,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final Object apiError;
        if (body == null) {
            String errorCode = exceptionCodeResolver.resolveExceptionCode(ex);
            apiError = apiErrorCreator.createApiError(request, errorCode, ex.getLocalizedMessage());
        } else {
            apiError = body;
        }
        return new ResponseEntity<>(apiError, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return handleBindingResult(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return handleBindingResult(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        if (ex.getCause() instanceof Exception) {
            return handleExceptionInternal((Exception) ex.getCause(), null, headers, status, request);
        } else {
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request) {
        return handleResultMessagesNotificationException(
                ex, null, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleBusinessException(final BusinessException ex, final WebRequest request) {
        return handleResultMessagesNotificationException(ex, null, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConcurrencyFailureException(final ConcurrencyFailureException ex,
            WebRequest request) {return handleExceptionInternal(ex, null, null, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleSystemError(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    private ResponseEntity<Object> handleBindingResult(final Exception ex, final BindingResult bindingResult,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String errorCode = exceptionCodeResolver.resolveExceptionCode(ex);
        final ApiError apiError = apiErrorCreator.createBindingResultApiError(request, errorCode, bindingResult, ex.getMessage());
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    private ResponseEntity<Object> handleResultMessagesNotificationException(final ResultMessagesNotificationException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String errorCode = exceptionCodeResolver.resolveExceptionCode(ex);
        final ApiError apiError = apiErrorCreator.createResultMessagesApiError(request, errorCode, ex.getResultMessages(), ex.getMessage());
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

}
