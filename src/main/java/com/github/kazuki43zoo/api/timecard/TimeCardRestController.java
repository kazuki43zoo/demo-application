package com.github.kazuki43zoo.api.timecard;

import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.TimeCard;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.github.kazuki43zoo.domain.service.timecard.TimeCardService;
import com.github.kazuki43zoo.domain.service.timecard.WorkPlaceSharedService;
import com.github.kazuki43zoo.web.security.CurrentUser;
import org.dozer.Mapper;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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
public class TimeCardRestController {

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @DateTimeFormat(pattern = "yyyy-MM")
    private @interface MonthFormat {
    }

    private final TimeCardRestController linkController = methodOn(getClass());

    @Inject
    TimeCardService timeCardService;

    @Inject
    WorkPlaceSharedService workPlaceSharedService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public TimeCardResource getTimeCard(
            @CurrentUser CustomUserDetails authenticatedUser,
            @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth) {

        // retrieve entity.
        TimeCard timeCard = timeCardService.getTimeCard(
                authenticatedUser.getAccount().getAccountUuid(), targetMonth);
        boolean stored = true;
        if (timeCard == null) {
            stored = false;
            timeCard = timeCardService.getDefaultTimeCard(
                    authenticatedUser.getAccount().getAccountUuid(), targetMonth);
        }
        WorkPlace defaultWorkPlace;
        if (timeCard.getWorkPlace() != null) {
            defaultWorkPlace = workPlaceSharedService.getWorkPlace(
                    timeCard.getWorkPlace().getWorkPlaceUuid());
        } else {
            defaultWorkPlace = workPlaceSharedService.getMainOffice();
        }

        // convert entity -> resource.
        TimeCardResource resource = beanMapper.map(timeCard, TimeCardResource.class);
        resource.setStored(stored);
        resource.setWorkPlace(defaultWorkPlace);

        // generate links.
        resource.add(linkTo(linkController.getTimeCard(
                authenticatedUser, targetMonth)).withSelfRel());
        for (DailyAttendanceResource attendance : resource.getAttendances()) {
            Link self = linkTo(linkController.getDailyAttendance(
                    authenticatedUser, targetMonth,
                    attendance.getTargetDate().getDayOfMonth())).withSelfRel();
            attendance.add(self);
        }

        return resource;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putTimeCard(
            @CurrentUser CustomUserDetails authenticatedUser,
            @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth,
            @RequestBody @Validated TimeCardResource resource) {

        // convert resource -> entity.
        TimeCard timeCard = beanMapper.map(resource, TimeCard.class);

        // save entity.
        timeCardService.saveTimeCard(
                authenticatedUser.getAccount().getAccountUuid(),
                targetMonth, timeCard);
    }

    @RequestMapping(value = "/{targetDay}", method = {RequestMethod.HEAD, RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource getDailyAttendance(
            @CurrentUser CustomUserDetails authenticatedUser,
            @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth,
            @PathVariable("targetDay") int targetDay) {

        // retrieve entity.
        DailyAttendance attendance = timeCardService.getDailyAttendance(
                authenticatedUser.getAccount().getAccountUuid(),
                targetMonth.plusDays(targetDay - 1));

        // convert entity -> resource.
        DailyAttendanceResource resource = beanMapper.map(
                attendance, DailyAttendanceResource.class);

        // generate link.
        resource.add(linkTo(linkController.getDailyAttendance(
                authenticatedUser, targetMonth, targetDay)).withSelfRel());
        return resource;
    }

    @RequestMapping(value = "/{targetDay}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putDailyAttendance(
            @CurrentUser CustomUserDetails authenticatedUser,
            @PathVariable("targetMonth") @MonthFormat LocalDate targetMonth,
            @PathVariable("targetDay") int targetDay,
            @RequestBody @Validated DailyAttendanceResource resource) {

        // convert resource -> entity.
        DailyAttendance attendance =
                beanMapper.map(resource, DailyAttendance.class);

        // save entity.
        timeCardService.saveDailyAttendance(
                authenticatedUser.getAccount().getAccountUuid(),
                targetMonth.plusDays(targetDay - 1), attendance);
    }

}
