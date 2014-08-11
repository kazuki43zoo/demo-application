package com.github.kazuki43zoo.domain.service.timecard;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.exception.SystemException;

import com.github.kazuki43zoo.core.message.Message;
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

    WorkPlace workPlaceOfMainOffice;

    @PostConstruct
    public void loadWorkPlaceOfMainOffice() {
        this.workPlaceOfMainOffice = workPlaceRepository.findOne(WorkPlace.MAIN_OFFICE_UUID);
        if (workPlaceOfMainOffice == null) {
            throw new SystemException(Message.FW_DATA_INCONSISTENCIES.code(),
                    "Workplace record of main office has not been inserted. uuid : "
                            + WorkPlace.MAIN_OFFICE_UUID);
        }
    }

    @Override
    public TimeCard getTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = timeCardRepository.findOne(accountUuid, targetMonth);
        if (timeCard != null) {
            WorkPlace defaultWorkPlace = decideDefaultWorkPlace(timeCard.getWorkPlace());
            for (DailyAttendance attendance : timeCard.getAttendances()) {
                attendance.setWorkPlace(getWorkPlaceDetail(attendance.getWorkPlace()));
                attendance.calculate(defaultWorkPlace);
            }
        }
        return timeCard;
    }

    @Override
    public TimeCard getDefaultTimeCard(String accountUuid, LocalDate targetMonth) {
        TimeCard timeCard = new TimeCard();
        WorkPlace defaultWorkPlace = decideDefaultWorkPlace(timeCard.getWorkPlace());
        for (int i = 0; i < targetMonth.dayOfMonth().getMaximumValue(); i++) {
            DailyAttendance attendance = new DailyAttendance();
            attendance.setTargetDate(targetMonth.plusDays(i));
            timeCard.addAttendance(attendance);
            attendance.calculate(defaultWorkPlace);
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
        if (StringUtils.hasLength(workPlaceUuid)) {
            return workPlaceRepository.findOne(workPlaceUuid);
        } else {
            return workPlaceOfMainOffice;
        }
    }

    @Override
    public WorkPlace getWorkPlaceDetail(WorkPlace workPlace) {
        if (workPlace == null) {
            return null;
        }
        String workPlaceUuid = workPlace.getWorkPlaceUuid();
        if (StringUtils.hasLength(workPlaceUuid)) {
            return workPlaceRepository.findOne(workPlaceUuid);
        } else {
            return null;
        }
    }

    private WorkPlace decideDefaultWorkPlace(WorkPlace defaultWorkPlace) {
        if (defaultWorkPlace != null) {
            return getWorkPlace(defaultWorkPlace.getWorkPlaceUuid());
        } else {
            return workPlaceOfMainOffice;
        }
    }

}
