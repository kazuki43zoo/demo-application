package com.kazuki43zoo.api.date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/dates")
@RestController
@lombok.RequiredArgsConstructor
public class DateRestController {

    private static final DateRestController LINK_CONTROLLER = methodOn(DateRestController.class);

    private final JodaTimeDateFactory dateFactory;

    @GetMapping(path = "/currentDateTime")
    @ResponseStatus(HttpStatus.OK)
    public DateResource getCurrentTimeDate() {
        final DateResource responseResource = new DateResource();
        responseResource.setDateTime(this.dateFactory.newDateTime());
        responseResource.add(linkTo(LINK_CONTROLLER.getCurrentTimeDate()).withSelfRel());
        return responseResource;
    }

}
