package com.kazuki43zoo.api.timecard;

import com.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.kazuki43zoo.domain.service.calendar.CalendarSharedService;
import com.kazuki43zoo.domain.service.timecard.WorkPlaceSharedService;
import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("timecards/calculate")
@Controller
@lombok.RequiredArgsConstructor
public class TimeCardCalculateController {

    private final WorkPlaceSharedService workPlaceSharedService;

    private final CalendarSharedService calendarSharedService;

    private final Mapper beanMapper;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource calculate(final @RequestBody @Validated DailyAttendanceResource resource) {
        final DailyAttendance attendance = this.beanMapper.map(resource, DailyAttendance.class);
        attendance.setWorkPlace(this.workPlaceSharedService.getWorkPlace(attendance.getWorkPlace().getWorkPlaceUuid()));
        attendance.calculate(null, this.workPlaceSharedService.getMainOffice(), this.calendarSharedService.getHolidays(attendance.getTargetDate().dayOfMonth().withMinimumValue()));
        return this.beanMapper.map(attendance, DailyAttendanceResource.class);
    }

    @GetMapping(params = "target=default")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DailyAttendanceResource getDefaultAttendanceResource(final @RequestParam(value = "workPlaceUuid", required = false) String workPlaceUuid) {
        final WorkPlace defaultWorkPlace = this.workPlaceSharedService.getWorkPlace(workPlaceUuid);

        final DailyAttendance attendance = new DailyAttendance();
        attendance.setDefault(defaultWorkPlace);
        attendance.calculate(defaultWorkPlace, this.workPlaceSharedService.getMainOffice());
        return this.beanMapper.map(attendance, DailyAttendanceResource.class);
    }
}
