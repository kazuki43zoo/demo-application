package com.github.kazuki43zoo.api.timecard;

import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.domain.service.calendar.CalendarSharedService;
import com.github.kazuki43zoo.domain.service.timecard.WorkPlaceSharedService;
import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RequestMapping("timecards/calculate")
@Controller
public class TimeCardCalculateController {

    @Inject
    WorkPlaceSharedService workPlaceSharedService;

    @Inject
    CalendarSharedService calendarSharedService;

    @Inject
    Mapper beanMapper;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource calculate(@RequestBody @Validated DailyAttendanceResource resource) {
        DailyAttendance attendance = beanMapper.map(resource, DailyAttendance.class);
        attendance.setWorkPlace(workPlaceSharedService.getWorkPlace(attendance.getWorkPlace().getWorkPlaceUuid()));
        attendance.calculate(null, workPlaceSharedService.getMainOffice(), calendarSharedService.getHolidays(attendance.getTargetDate().dayOfMonth().withMinimumValue()));
        return beanMapper.map(attendance, DailyAttendanceResource.class);
    }

    @RequestMapping(method = RequestMethod.GET, params = "target=default")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource getDefaultAttendanceResource(@RequestParam(value = "workPlaceUuid", required = false) String workPlaceUuid) {
        WorkPlace defaultWorkPlace = workPlaceSharedService.getWorkPlace(workPlaceUuid);

        DailyAttendance attendance = new DailyAttendance();
        attendance.setDefault(defaultWorkPlace);
        attendance.calculate(defaultWorkPlace, workPlaceSharedService.getMainOffice());
        return beanMapper.map(attendance, DailyAttendanceResource.class);
    }
}
