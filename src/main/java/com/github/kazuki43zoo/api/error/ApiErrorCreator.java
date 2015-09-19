package com.github.kazuki43zoo.api.error;

import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.inject.Inject;

@Component
public class ApiErrorCreator {

    @Inject
    MessageSource messageSource;

    public ApiError createApiError(final WebRequest request, final String errorCode, final String defaultErrorMessage, final Object... arguments) {
        final String localizedMessage = messageSource.getMessage(
                errorCode, arguments, defaultErrorMessage, request.getLocale());
        return new ApiError(errorCode, localizedMessage);
    }

    public ApiError createBindingResultApiError(final WebRequest request, final String errorCode, final BindingResult bindingResult, final String defaultErrorMessage) {
        final ApiError apiError = createApiError(request, errorCode, defaultErrorMessage);
        for (final FieldError fieldError : bindingResult.getFieldErrors()) {
            apiError.addDetail(createApiError(request, fieldError, fieldError.getField()));
        }
        for (final ObjectError objectError : bindingResult.getGlobalErrors()) {
            apiError.addDetail(createApiError(request, objectError, objectError.getObjectName()));
        }
        return apiError;
    }

    private ApiError createApiError(final WebRequest request, final DefaultMessageSourceResolvable messageResolvable, final String target) {
        final String localizedMessage = messageSource.getMessage(messageResolvable, request.getLocale());
        return new ApiError(messageResolvable.getCode(), localizedMessage, target);
    }

    public ApiError createResultMessagesApiError(final WebRequest request, final String rootErrorCode, final ResultMessages resultMessages, final String defaultErrorMessage) {
        final ApiError apiError;
        if (resultMessages.getList().size() == 1) {
            final ResultMessage resultMessage = resultMessages.iterator().next();
            String errorCode = resultMessage.getCode();
            final String errorText = resultMessage.getText();
            if (errorCode == null && errorText == null) {
                errorCode = rootErrorCode;
            }
            apiError = createApiError(request, errorCode, errorText, resultMessage.getArgs());
        } else {
            apiError = createApiError(request, rootErrorCode, defaultErrorMessage);
            for (final ResultMessage resultMessage : resultMessages.getList()) {
                apiError.addDetail(createApiError(request, resultMessage.getCode(), resultMessage.getText(), resultMessage.getArgs()));
            }
        }
        return apiError;
    }

}
