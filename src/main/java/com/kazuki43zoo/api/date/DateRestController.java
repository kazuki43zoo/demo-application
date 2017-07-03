package com.kazuki43zoo.api.date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import javax.inject.Inject;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("/dates")
@RestController
public class DateRestController {

    private final DateRestController linkController = methodOn(getClass());

    @Inject
    JodaTimeDateFactory dateFactory;

    @GetMapping(path = "/currentDateTime")
    @ResponseStatus(HttpStatus.OK)
    public DateResource getCurrentTimeDate() {
        final DateResource responseResource = new DateResource();
        responseResource.setDateTime(dateFactory.newDateTime());
        responseResource.add(linkTo(linkController.getCurrentTimeDate()).withSelfRel());
        return responseResource;
    }

}
