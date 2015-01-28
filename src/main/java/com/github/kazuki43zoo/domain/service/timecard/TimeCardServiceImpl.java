package com.github.kazuki43zoo.domain.service.timecard;

import com.github.kazuki43zoo.domain.model.calendar.Holiday;
import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.TimeCard;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.domain.repository.timecard.TimeCardRepository;
import com.github.kazuki43zoo.domain.service.calendar.CalendarSharedService;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Transactional
@Service
public final class TimeCardServiceImpl implements TimeCardService {

    @lombok.NonNull
    private final WorkPlaceSharedService workPlaceSharedService;

    @lombok.NonNull
    private final CalendarSharedService calendarSharedService;

    @lombok.NonNull
    private final TimeCardRepository timeCardRepository;

    @Inject
    public TimeCardServiceImpl(WorkPlaceSharedService workPlaceSharedService, CalendarSharedService calendarSharedService, @Named("timeCardBatchModeRepository") TimeCardRepository timeCardRepository) {
        this.workPlaceSharedService = workPlaceSharedService;
        this.calendarSharedService = calendarSharedService;
        this.timeCardRepository = timeCardRepository;
    }

    @Override
    public TimeCard getTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        if (timeCard != null) {
            String defaultWorkPlaceUuid = null;
            if (timeCard.getWorkPlace() != null) {
                defaultWorkPlaceUuid = timeCard.getWorkPlace().getWorkPlaceUuid();
            }
            WorkPlace defaultWorkPlace = workPlaceSharedService.getWorkPlace(defaultWorkPlaceUuid);
            WorkPlace mainOffice = workPlaceSharedService.getMainOffice();
            Map<LocalDate, Holiday> holidaies = calendarSharedService.getHolodaies(targetMonth);
            for (DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setWorkPlace(workPlaceSharedService.getWorkPlaceDetail(attendance
                        .getWorkPlace()));
                attendance.calculate(defaultWorkPlace, mainOffice, holidaies);
            }
        }
        return timeCard;
    }

    @Override
    public TimeCard getDefaultTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = new TimeCard();
        WorkPlace defaultWorkPlace = workPlaceSharedService.getWorkPlace(null);
        WorkPlace mainOffice = workPlaceSharedService.getMainOffice();
        Map<LocalDate, Holiday> holidaies = calendarSharedService.getHolodaies(targetMonth);
        for (int i = 0; i < targetMonth.dayOfMonth().getMaximumValue(); i++) {
            DailyAttendance attendance = new DailyAttendance();
            attendance.setTargetDate(targetMonth.plusDays(i));
            timeCard.addAttendance(attendance);
            attendance.calculate(defaultWorkPlace, mainOffice, holidaies);
        }
        return timeCard;
    }

    @Override
    public void saveTimeCard(String accountUuid, LocalDate targetMonth, TimeCard timeCard) {
        TimeCard loadedTimeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        timeCard.setAccountUuid(accountUuid);
        timeCard.setTargetMonth(targetMonth);
        if (loadedTimeCard == null) {
            timeCardRepository.create(timeCard);
            for (DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setAccountUuid(accountUuid);
                timeCardRepository.createDailyAttendance(attendance);
            }
        } else {
            timeCardRepository.update(timeCard);
            for (DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setAccountUuid(accountUuid);
                timeCardRepository.updateDailyAttendance(attendance);
            }
        }
    }

    @Override
    public void saveDailyAttendance(String accountUuid, LocalDate targetDate,
                                    DailyAttendance attendance) {
        DailyAttendance loadedAttendance = timeCardRepository.findOneDailyAttendance(accountUuid,
                targetDate);
        if (loadedAttendance == null) {
            attendance.setAccountUuid(accountUuid);
            timeCardRepository.createDailyAttendance(attendance);
        } else {
            attendance.setAccountUuid(accountUuid);
            timeCardRepository.updateDailyAttendance(attendance);
        }
    }

    @Override
    public DailyAttendance getDailyAttendance(String accountUuid, LocalDate targetDate) {
        return timeCardRepository.findOneDailyAttendance(accountUuid, targetDate);
    }

}
