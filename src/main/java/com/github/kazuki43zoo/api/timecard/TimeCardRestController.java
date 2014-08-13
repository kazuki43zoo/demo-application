package com.github.kazuki43zoo.api.timecard;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.TimeCard;
import com.github.kazuki43zoo.domain.service.security.CustomUserDetails;
import com.github.kazuki43zoo.domain.service.timecard.TimeCardService;
import com.github.kazuki43zoo.web.security.CurrentUser;

@RequestMapping("timecards/{targetMonth}")
@RestController
public class TimeCardRestController {

    @Inject
    TimeCardService timeCardService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = { RequestMethod.HEAD, RequestMethod.GET })
    @ResponseStatus(HttpStatus.OK)
    public TimeCardResource getTimeCard(@CurrentUser CustomUserDetails currentUser,
            @PathVariable("targetMonth") @DateTimeFormat(pattern = "yyyy-MM") LocalDate targetMonth) {
        TimeCard timeCard = timeCardService.getTimeCard(currentUser.getAccount().getAccountUuid(),
                targetMonth);
        boolean stored = true;
        if (timeCard == null) {
            stored = false;
            timeCard = timeCardService.getDefaultTimeCard(
                    currentUser.getAccount().getAccountUuid(), targetMonth);
        }
        TimeCardResource resource = beanMapper.map(timeCard, TimeCardResource.class);
        resource.setStored(stored);
        return resource;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putTimeCard(
            @CurrentUser CustomUserDetails currentUser,
            @PathVariable("targetMonth") @DateTimeFormat(pattern = "yyyy-MM") LocalDate targetMonth,
            @RequestBody @Validated TimeCardResource resource) {
        TimeCard timeCard = beanMapper.map(resource, TimeCard.class);
        timeCardService.saveTimeCard(currentUser.getAccount().getAccountUuid(), targetMonth,
                timeCard);
    }

    @RequestMapping(value = "{targetDay}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putDailyAttendance(
            @CurrentUser CustomUserDetails currentUser,
            @PathVariable("targetMonth") @DateTimeFormat(pattern = "yyyy-MM") LocalDate targetMonth,
            @PathVariable("targetDay") int targetDay,
            @RequestBody @Validated DailyAttendanceResource resource) {
        DailyAttendance attendance = beanMapper.map(resource, DailyAttendance.class);
        timeCardService.saveDailyAttendance(currentUser.getAccount().getAccountUuid(),
                targetMonth.plusDays(targetDay - 1), attendance);
    }

}
