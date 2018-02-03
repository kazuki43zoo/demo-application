package com.kazuki43zoo.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.RequestDispatcher;

@RequestMapping("error")
@RestController
@lombok.RequiredArgsConstructor
public class ApiErrorPageController {

    private final ApiErrorCreator apiErrorCreator;

    @RequestMapping
    public ResponseEntity<ApiError> handleErrorPage(final @RequestParam("errorCode") String errorCode, final WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.valueOf((Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, RequestAttributes.SCOPE_REQUEST));
        final ApiError apiError = this.apiErrorCreator.createApiError(request, errorCode, httpStatus.getReasonPhrase());
        return new ResponseEntity<>(apiError, httpStatus);
    }

}
