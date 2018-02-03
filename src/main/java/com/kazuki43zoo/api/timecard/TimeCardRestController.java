package com.kazuki43zoo.api.timecard;

import com.kazuki43zoo.core.security.CurrentUser;
import com.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.kazuki43zoo.domain.model.timecard.TimeCard;
import com.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.kazuki43zoo.domain.service.timecard.TimeCardService;
import com.kazuki43zoo.domain.service.timecard.WorkPlaceSharedService;
import org.dozer.Mapper;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * REST API for time card resource.
 */
@RequestMapping("/timecards/{targetMonth}")
@RestController
@lombok.RequiredArgsConstructor
public class TimeCardRestController {

    private static final TimeCardRestController LINK_CONTROLLER = methodOn(TimeCardRestController.class);

    private final TimeCardService timeCardService;

    private final WorkPlaceSharedService workPlaceSharedService;

    private final Mapper beanMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TimeCardResource getTimeCard(final @CurrentUser CustomUserDetails authenticatedUser, final @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth) {

        // retrieve entity.
        TimeCard timeCard = this.timeCardService.getTimeCard(authenticatedUser.getAccount().getAccountUuid(), targetMonth);
        boolean stored = true;
        if (timeCard == null) {
            stored = false;
            timeCard = this.timeCardService.getDefaultTimeCard(authenticatedUser.getAccount().getAccountUuid(), targetMonth);
        }
        final WorkPlace defaultWorkPlace;
        if (timeCard.getWorkPlace() != null) {
            defaultWorkPlace = this.workPlaceSharedService.getWorkPlace(timeCard.getWorkPlace().getWorkPlaceUuid());
        } else {
            defaultWorkPlace = this.workPlaceSharedService.getMainOffice();
        }

        // convert entity -> resource.
        final TimeCardResource resource = this.beanMapper.map(timeCard, TimeCardResource.class);
        resource.setStored(stored);
        resource.setWorkPlace(defaultWorkPlace);

        // generate links.
        resource.add(linkTo(LINK_CONTROLLER.getTimeCard(authenticatedUser, targetMonth)).withSelfRel());
        for (final DailyAttendanceResource attendance : resource.getAttendances()) {
            final Link self = linkTo(LINK_CONTROLLER.getDailyAttendance(authenticatedUser, targetMonth, attendance.getTargetDate().getDayOfMonth())).withSelfRel();
            attendance.add(self);
        }

        return resource;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putTimeCard(final @CurrentUser CustomUserDetails authenticatedUser, final @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth, final @RequestBody @Validated TimeCardResource resource) {

        // convert resource -> entity.
        final TimeCard timeCard = this.beanMapper.map(resource, TimeCard.class);

        // save entity.
        this.timeCardService.saveTimeCard(authenticatedUser.getAccount().getAccountUuid(), targetMonth, timeCard);
    }

    @GetMapping(path = "/{targetDay}")
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource getDailyAttendance(final @CurrentUser CustomUserDetails authenticatedUser, final @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth, final @PathVariable("targetDay") int targetDay) {

        // retrieve entity.
        final DailyAttendance attendance = this.timeCardService.getDailyAttendance(authenticatedUser.getAccount().getAccountUuid(), targetMonth.plusDays(targetDay - 1));

        // convert entity -> resource.
        final DailyAttendanceResource resource = this.beanMapper.map(attendance, DailyAttendanceResource.class);

        // generate link.
        resource.add(linkTo(LINK_CONTROLLER.getDailyAttendance(authenticatedUser, targetMonth, targetDay)).withSelfRel());
        return resource;
    }

    @PutMapping(path = "/{targetDay}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putDailyAttendance(final @CurrentUser CustomUserDetails authenticatedUser, final @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth, final @PathVariable("targetDay") int targetDay, final @RequestBody @Validated DailyAttendanceResource resource) {

        // convert resource -> entity.
        final DailyAttendance attendance = this.beanMapper.map(resource, DailyAttendance.class);

        // save entity.
        this.timeCardService.saveDailyAttendance(authenticatedUser.getAccount().getAccountUuid(), targetMonth.plusDays(targetDay - 1), attendance);
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @DateTimeFormat(pattern = "yyyy-MM")
    private @interface MonthFormat {
    }

}
