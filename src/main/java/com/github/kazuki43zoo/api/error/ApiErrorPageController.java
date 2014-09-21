package com.github.kazuki43zoo.api.error;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RequestMapping("error")
@RestController
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ApiErrorPageController {

    private final @lombok.NonNull ApiErrorCreator apiErrorCreator;

    @RequestMapping
    public ResponseEntity<ApiError> handleErrorPage(@RequestParam("errorCode") String errorCode,
            WebRequest request) {
        HttpStatus httpStatus = HttpStatus.valueOf((Integer) request.getAttribute(
                RequestDispatcher.ERROR_STATUS_CODE, RequestAttributes.SCOPE_REQUEST));
        ApiError apiError = apiErrorCreator.createApiError(request, errorCode,
                httpStatus.getReasonPhrase());
        return new ResponseEntity<>(apiError, httpStatus);
    }

}
