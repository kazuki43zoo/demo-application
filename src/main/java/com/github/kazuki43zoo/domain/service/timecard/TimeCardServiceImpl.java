package com.github.kazuki43zoo.domain.service.timecard;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.github.kazuki43zoo.domain.model.timecard.DailyAttendance;
import com.github.kazuki43zoo.domain.model.timecard.TimeCard;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.domain.repository.timecard.TimeCardRepository;

@Transactional
@Service
public class TimeCardServiceImpl implements TimeCardService {

    @Inject
    WorkPlaceService workPlaceService;

    @Inject
    @Named("timeCardBatchModeRepository")
    TimeCardRepository timeCardRepository;

    @Override
    public TimeCard getTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        if (timeCard != null) {
            String defaultWorkPlaceUuid = null;
            if (timeCard.getWorkPlace() != null) {
                defaultWorkPlaceUuid = timeCard.getWorkPlace().getWorkPlaceUuid();
            }
            WorkPlace defaultWorkPlace = workPlaceService.getWorkPlace(defaultWorkPlaceUuid);
            WorkPlace mainOffice = workPlaceService.getMainOffice();
            for (DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setWorkPlace(workPlaceService.getWorkPlaceDetail(attendance
                        .getWorkPlace()));
                attendance.calculate(defaultWorkPlace, mainOffice);
            }
        }
        return timeCard;
    }

    @Override
    public TimeCard getDefaultTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = new TimeCard();
        WorkPlace defaultWorkPlace = workPlaceService.getWorkPlace(null);
        WorkPlace mainOffice = workPlaceService.getMainOffice();
        for (int i = 0; i < targetMonth.dayOfMonth().getMaximumValue(); i++) {
            DailyAttendance attendance = new DailyAttendance();
            attendance.setTargetDate(targetMonth.plusDays(i));
            timeCard.addAttendance(attendance);
            attendance.calculate(defaultWorkPlace, mainOffice);
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

}
