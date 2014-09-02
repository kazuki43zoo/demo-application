package com.github.kazuki43zoo.api.date;

import javax.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.terasoluna.gfw.common.date.DateFactory;

@RequestMapping("dates")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DateRestController {

    private final @NonNull DateFactory dateFactory;

    @RequestMapping(value = "currentDateTime", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DateResource getCurrentTimeDate() {
        DateResource responseResource = new DateResource();
        responseResource.setDateTime(dateFactory.newDateTime());
        return responseResource;
    }

}
