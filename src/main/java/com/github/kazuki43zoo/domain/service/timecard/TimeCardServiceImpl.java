package com.github.kazuki43zoo.domain.service.timecard;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.kazuki43zoo.domain.model.DailyAttendance;
import com.github.kazuki43zoo.domain.model.TimeCard;
import com.github.kazuki43zoo.domain.model.WorkPlace;
import com.github.kazuki43zoo.domain.repository.timecard.TimeCardRepository;
import com.github.kazuki43zoo.domain.repository.timecard.WorkPlaceRepository;

@Transactional
@Service
public class TimeCardServiceImpl implements TimeCardService {

    @Inject
    TimeCardRepository timeCardRepository;

    @Inject
    WorkPlaceRepository workPlaceRepository;

    @Override
    public TimeCard getTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        if (timeCard != null) {
            for (DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.calculate(timeCard.getWorkPlace());
            }
        }
        return timeCard;
    }

    @Override
    public TimeCard getDefaultTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = new TimeCard();
        for (int i = 0; i < targetMonth.dayOfMonth().getMaximumValue(); i++) {
            DailyAttendance attendance = new DailyAttendance();
            attendance.setTargetDate(targetMonth.plusDays(i));
            attendance.calculate(timeCard.getWorkPlace());
            timeCard.addAttendance(attendance);
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
        } else {
            timeCardRepository.update(timeCard);
        }
        if (timeCard.getAttendances() == null) {
            return;
        }
        for (DailyAttendance attendance : timeCard.getAttendances()) {
            saveDailyAttendance(accountUuid, attendance.getTargetDate(), attendance);
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
    public WorkPlace getWorkPlace(String workPlaceUuid) {
        WorkPlace workPlace = null;
        if (StringUtils.hasLength(workPlaceUuid)) {
            workPlace = workPlaceRepository.findOne(workPlaceUuid);
        }
        return workPlace;
    }

}
