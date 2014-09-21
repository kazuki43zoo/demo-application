package com.github.kazuki43zoo.api.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@lombok.Data
@lombok.RequiredArgsConstructor
public class ApiError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String message;

    @JsonInclude(Include.NON_EMPTY)
    private final String target;

    @JsonInclude(Include.NON_EMPTY)
    private final List<ApiError> details = new ArrayList<>();

    public ApiError(String code, String message) {
        this(code, message, null);
    }

    public void addDetail(ApiError detail) {
        details.add(detail);
    }

}
