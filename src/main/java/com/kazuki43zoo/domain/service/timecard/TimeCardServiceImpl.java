package com.kazuki43zoo.domain.service.timecard;

import com.kazuki43zoo.domain.model.calendar.Holiday;
import com.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.kazuki43zoo.domain.model.timecard.TimeCard;
import com.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.kazuki43zoo.domain.repository.timecard.TimeCardRepository;
import com.kazuki43zoo.domain.service.calendar.CalendarSharedService;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Transactional
@Service
public final class TimeCardServiceImpl implements TimeCardService {

    @Inject
    WorkPlaceSharedService workPlaceSharedService;

    @Inject
    CalendarSharedService calendarSharedService;

    @Inject
    @Named("timeCardBatchModeRepository")
    TimeCardRepository timeCardRepository;


    @Override
    public TimeCard getTimeCard(final String accountUuid, final LocalDate targetMonth) {
        final TimeCard timeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        if (timeCard != null) {
            String defaultWorkPlaceUuid = null;
            if (timeCard.getWorkPlace() != null) {
                defaultWorkPlaceUuid = timeCard.getWorkPlace().getWorkPlaceUuid();
            }
            final WorkPlace defaultWorkPlace = workPlaceSharedService.getWorkPlace(defaultWorkPlaceUuid);
            final WorkPlace mainOffice = workPlaceSharedService.getMainOffice();
            final Map<LocalDate, Holiday> holidays = calendarSharedService.getHolidays(targetMonth);
            for (final DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setWorkPlace(workPlaceSharedService.getWorkPlaceDetail(attendance.getWorkPlace()));
                attendance.calculate(defaultWorkPlace, mainOffice, holidays);
            }
        }
        return timeCard;
    }

    @Override
    public TimeCard getDefaultTimeCard(final String accountUuid, final LocalDate targetMonth) {
        final TimeCard timeCard = new TimeCard();
        final WorkPlace defaultWorkPlace = workPlaceSharedService.getWorkPlace(null);
        final WorkPlace mainOffice = workPlaceSharedService.getMainOffice();
        final Map<LocalDate, Holiday> holidays = calendarSharedService.getHolidays(targetMonth);
        for (int i = 0; i < targetMonth.dayOfMonth().getMaximumValue(); i++) {
            final DailyAttendance attendance = new DailyAttendance();
            attendance.setTargetDate(targetMonth.plusDays(i));
            attendance.calculate(defaultWorkPlace, mainOffice, holidays);
            timeCard.addAttendance(attendance);
        }
        return timeCard;
    }

    @Override
    public void saveTimeCard(final String accountUuid, final LocalDate targetMonth, final TimeCard timeCard) {
        final TimeCard loadedTimeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        timeCard.setAccountUuid(accountUuid);
        timeCard.setTargetMonth(targetMonth);
        if (loadedTimeCard == null) {
            timeCardRepository.create(timeCard);
            for (final DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setAccountUuid(accountUuid);
                timeCardRepository.createDailyAttendance(attendance);
            }
        } else {
            timeCardRepository.update(timeCard);
            for (final DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setAccountUuid(accountUuid);
                timeCardRepository.updateDailyAttendance(attendance);
            }
        }
    }

    @Override
    public void saveDailyAttendance(final String accountUuid, final LocalDate targetDate, final DailyAttendance attendance) {
        final DailyAttendance loadedAttendance = timeCardRepository.findOneDailyAttendance(accountUuid, targetDate);
        if (loadedAttendance == null) {
            attendance.setAccountUuid(accountUuid);
            timeCardRepository.createDailyAttendance(attendance);
        } else {
            attendance.setAccountUuid(accountUuid);
            timeCardRepository.updateDailyAttendance(attendance);
        }
    }

    @Override
    public DailyAttendance getDailyAttendance(final String accountUuid, final LocalDate targetDate) {
        return timeCardRepository.findOneDailyAttendance(accountUuid, targetDate);
    }

}
