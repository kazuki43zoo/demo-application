package com.github.kazuki43zoo.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;

@RequestMapping("error")
@RestController
public class ApiErrorPageController {

    @Inject
    ApiErrorCreator apiErrorCreator;

    @RequestMapping
    public ResponseEntity<ApiError> handleErrorPage(final @RequestParam("errorCode") String errorCode, final WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.valueOf((Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, RequestAttributes.SCOPE_REQUEST));
        final ApiError apiError = apiErrorCreator.createApiError(request, errorCode, httpStatus.getReasonPhrase());
        return new ResponseEntity<>(apiError, httpStatus);
    }

}
