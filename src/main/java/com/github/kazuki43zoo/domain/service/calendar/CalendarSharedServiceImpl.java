package com.github.kazuki43zoo.domain.service.calendar;

import com.github.kazuki43zoo.domain.model.calendar.FixedHoliday;
import com.github.kazuki43zoo.domain.model.calendar.HappyMondayHoliday;
import com.github.kazuki43zoo.domain.model.calendar.Holiday;
import com.github.kazuki43zoo.domain.model.calendar.SeasonalHoliday;
import com.github.kazuki43zoo.domain.repository.calendar.FixedHolidayRepository;
import com.github.kazuki43zoo.domain.repository.calendar.HappyMondayHolidayRepository;
import com.github.kazuki43zoo.domain.repository.calendar.SeasonalHolidayRepository;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Service
public final class CalendarSharedServiceImpl implements CalendarSharedService {

    @Inject
    FixedHolidayRepository fixedHolidayRepository;

    @Inject
    HappyMondayHolidayRepository happyMondayHolidayRepository;

    @Inject
    SeasonalHolidayRepository seasonalHolidayRepository;

    private Map<Integer, List<FixedHoliday>> fixedHolidaysMapping;

    private Map<Integer, List<HappyMondayHoliday>> happyMondayHolidaysMapping;

    private Map<LocalDate, List<SeasonalHoliday>> seasonalHolidaysMapping;

    @PostConstruct
    public void loadHolidays() {
        loadFixedHolidays();
        loadHappyMondayHolidays();
        loadSeasonalHolidays();
    }

    private void loadFixedHolidays() {
        Map<Integer, List<FixedHoliday>> fixedHolidaysMapping = new HashMap<>();
        List<FixedHoliday> fixedHolidays = fixedHolidayRepository.findAll();
        for (FixedHoliday fixedHoliday : fixedHolidays) {
            List<FixedHoliday> fixedHolidaysOfMonth =
                    fixedHolidaysMapping.get(fixedHoliday.getTargetMonth());
            if (fixedHolidaysOfMonth == null) {
                fixedHolidaysOfMonth = new ArrayList<>();
                fixedHolidaysMapping.put(fixedHoliday.getTargetMonth(), fixedHolidaysOfMonth);
            }
            fixedHolidaysOfMonth.add(fixedHoliday);
        }
        this.fixedHolidaysMapping = Collections.unmodifiableMap(fixedHolidaysMapping);
    }

    private void loadHappyMondayHolidays() {
        Map<Integer, List<HappyMondayHoliday>> happyMondayHolidaysMapping = new HashMap<>();
        List<HappyMondayHoliday> happyMondayHolidays = happyMondayHolidayRepository.findAll();
        for (HappyMondayHoliday happyMondayHoliday : happyMondayHolidays) {
            List<HappyMondayHoliday> happyMondayHolidaysOfMonth =
                    happyMondayHolidaysMapping.get(happyMondayHoliday.getTargetMonth());
            if (happyMondayHolidaysOfMonth == null) {
                happyMondayHolidaysOfMonth = new ArrayList<>();
                happyMondayHolidaysMapping.put(happyMondayHoliday.getTargetMonth(), happyMondayHolidaysOfMonth);
            }
            happyMondayHolidaysOfMonth.add(happyMondayHoliday);
        }
        this.happyMondayHolidaysMapping = Collections.unmodifiableMap(happyMondayHolidaysMapping);
    }

    private void loadSeasonalHolidays() {
        Map<LocalDate, List<SeasonalHoliday>> seasonalHolidaysMapping = new HashMap<>();
        List<SeasonalHoliday> seasonalHolidays = seasonalHolidayRepository.findAll();
        for (SeasonalHoliday seasonalHoliday : seasonalHolidays) {
            LocalDate month = seasonalHoliday.getTargetDate().dayOfMonth().withMinimumValue();
            List<SeasonalHoliday> seasonalHolidaysOfMonth = seasonalHolidaysMapping.get(month);
            if (seasonalHolidaysOfMonth == null) {
                seasonalHolidaysOfMonth = new ArrayList<>();
                seasonalHolidaysMapping.put(month, seasonalHolidaysOfMonth);
            }
            seasonalHolidaysOfMonth.add(seasonalHoliday);
        }
        this.seasonalHolidaysMapping = Collections.unmodifiableMap(seasonalHolidaysMapping);
    }

    @Override
    public Map<LocalDate, Holiday> getHolidays(LocalDate month) {
        Map<LocalDate, Holiday> holidays = new LinkedHashMap<>();
        pickupFixedHolidays(month, holidays);
        pickupHappyMondayHolidays(month, holidays);
        pickupSeasonalHolidays(month, holidays);
        return holidays;
    }

    private void pickupFixedHolidays(LocalDate month, Map<LocalDate, Holiday> holidays) {
        List<FixedHoliday> fixedHolidays = fixedHolidaysMapping.get(month.getMonthOfYear());
        if (fixedHolidays == null) {
            return;
        }
        for (FixedHoliday fixedHoliday : fixedHolidays) {
            if (!((fixedHoliday.getBeginYear() <= month.getYear()) && (month.getYear() <= fixedHoliday.getEndYear()))) {
                continue;
            }
            LocalDate targetDate = month.withDayOfMonth(fixedHoliday.getTargetDay());
            if (targetDate.getDayOfWeek() == DateTimeConstants.SUNDAY) {
                targetDate = targetDate.plusDays(fixedHoliday.getTransferredLaterDays());
            }
            holidays.put(targetDate, fixedHoliday);
        }
    }

    private void pickupHappyMondayHolidays(LocalDate month, Map<LocalDate, Holiday> holidaies) {
        List<HappyMondayHoliday> happyMondayHolidays = happyMondayHolidaysMapping.get(month
                .getMonthOfYear());
        if (happyMondayHolidays == null) {
            return;
        }
        for (HappyMondayHoliday happyMondayHoliday : happyMondayHolidays) {
            if (!((happyMondayHoliday.getBeginYear() <= month.getYear()) && (month.getYear() <= happyMondayHoliday.getEndYear()))) {
                continue;
            }
            LocalDate targetDate;
            int plusWeeks = happyMondayHoliday.getTargetWeek();
            if (month.getDayOfWeek() == DateTimeConstants.MONDAY) {
                plusWeeks--;
            }
            targetDate = month.plusWeeks(plusWeeks).dayOfWeek().withMinimumValue();
            holidaies.put(targetDate, happyMondayHoliday);
        }
    }

    private void pickupSeasonalHolidays(LocalDate month, Map<LocalDate, Holiday> holidays) {
        List<SeasonalHoliday> seasonalHolidays = seasonalHolidaysMapping.get(month);
        if (seasonalHolidays == null) {
            return;
        }
        for (SeasonalHoliday seasonalHoliday : seasonalHolidays) {
            holidays.put(seasonalHoliday.getTargetDate(), seasonalHoliday);
        }
    }

}
