package com.github.kazuki43zoo.api.timecard;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.kazuki43zoo.domain.model.DailyAttendance;
import com.github.kazuki43zoo.domain.model.WorkPlace;
import com.github.kazuki43zoo.domain.service.timecard.TimeCardService;

@RequestMapping("timecards/calculate")
@Controller
public class TimeCardCalculateController {

    @Inject
    TimeCardService timeCardService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource calculate(
            @RequestBody @Validated DailyAttendanceResource resource) {
        DailyAttendance attendance = beanMapper.map(resource, DailyAttendance.class);
        attendance.setWorkPlace(timeCardService.getWorkPlace(attendance.getWorkPlace()
                .getWorkPlaceUuid()));
        attendance.calculate();
        return beanMapper.map(attendance, DailyAttendanceResource.class);
    }

    @RequestMapping(method = RequestMethod.GET, params = "target=default")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource getDefaultAttendanceResource(
            @RequestParam(value = "workPlaceUuid", required = false) String workPlaceUuid) {
        WorkPlace defaultWorkPlace = timeCardService.getWorkPlace(workPlaceUuid);

        DailyAttendance attendance = new DailyAttendance();
        attendance.setDefault(defaultWorkPlace);
        attendance.calculate(defaultWorkPlace);
        return beanMapper.map(attendance, DailyAttendanceResource.class);
    }
}
