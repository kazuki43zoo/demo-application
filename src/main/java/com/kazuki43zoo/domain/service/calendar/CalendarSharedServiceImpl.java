package com.kazuki43zoo.domain.service.calendar;

import com.kazuki43zoo.domain.model.calendar.FixedHoliday;
import com.kazuki43zoo.domain.model.calendar.HappyMondayHoliday;
import com.kazuki43zoo.domain.model.calendar.Holiday;
import com.kazuki43zoo.domain.model.calendar.SeasonalHoliday;
import com.kazuki43zoo.domain.repository.calendar.FixedHolidayRepository;
import com.kazuki43zoo.domain.repository.calendar.HappyMondayHolidayRepository;
import com.kazuki43zoo.domain.repository.calendar.SeasonalHolidayRepository;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@lombok.RequiredArgsConstructor
public class CalendarSharedServiceImpl implements CalendarSharedService {

    private final FixedHolidayRepository fixedHolidayRepository;

    private final HappyMondayHolidayRepository happyMondayHolidayRepository;

    private final SeasonalHolidayRepository seasonalHolidayRepository;

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
        final Map<Integer, List<FixedHoliday>> fixedHolidaysMapping = new HashMap<>();
        final List<FixedHoliday> fixedHolidays = this.fixedHolidayRepository.findAll();
        for (final FixedHoliday fixedHoliday : fixedHolidays) {
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
        final Map<Integer, List<HappyMondayHoliday>> happyMondayHolidaysMapping = new HashMap<>();
        final List<HappyMondayHoliday> happyMondayHolidays = this.happyMondayHolidayRepository.findAll();
        for (final HappyMondayHoliday happyMondayHoliday : happyMondayHolidays) {
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
        final Map<LocalDate, List<SeasonalHoliday>> seasonalHolidaysMapping = new HashMap<>();
        final List<SeasonalHoliday> seasonalHolidays = this.seasonalHolidayRepository.findAll();
        for (final SeasonalHoliday seasonalHoliday : seasonalHolidays) {
            final LocalDate month = seasonalHoliday.getTargetDate().dayOfMonth().withMinimumValue();
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
    public Map<LocalDate, Holiday> getHolidays(final LocalDate month) {
        final Map<LocalDate, Holiday> holidays = new LinkedHashMap<>();
        pickupFixedHolidays(month, holidays);
        pickupHappyMondayHolidays(month, holidays);
        pickupSeasonalHolidays(month, holidays);
        return holidays;
    }

    private void pickupFixedHolidays(final LocalDate month, final Map<LocalDate, Holiday> holidays) {
        final List<FixedHoliday> fixedHolidays = this.fixedHolidaysMapping.get(month.getMonthOfYear());
        if (fixedHolidays == null) {
            return;
        }
        for (final FixedHoliday fixedHoliday : fixedHolidays) {
            if (!((fixedHoliday.getBeginYear() <= month.getYear()) && (month.getYear() <= fixedHoliday.getEndYear()))) {
                continue;
            }
            LocalDate targetDate = month.withDayOfMonth(fixedHoliday.getTargetDay());
            if (targetDate.getDayOfWeek() == DateTimeConstants.SUNDAY && fixedHoliday.getTransferredLaterDays() != null) {
                targetDate = targetDate.plusDays(fixedHoliday.getTransferredLaterDays());
            }
            holidays.put(targetDate, fixedHoliday);
        }
    }

    private void pickupHappyMondayHolidays(final LocalDate month, final Map<LocalDate, Holiday> holidays) {
        final List<HappyMondayHoliday> happyMondayHolidays = this.happyMondayHolidaysMapping.get(month.getMonthOfYear());
        if (happyMondayHolidays == null) {
            return;
        }
        for (final HappyMondayHoliday happyMondayHoliday : happyMondayHolidays) {
            if (!((happyMondayHoliday.getBeginYear() <= month.getYear()) && (month.getYear() <= happyMondayHoliday.getEndYear()))) {
                continue;
            }
            int plusWeeks = happyMondayHoliday.getTargetWeek();
            if (month.getDayOfWeek() == DateTimeConstants.MONDAY) {
                plusWeeks--;
            }
            final LocalDate targetDate = month.plusWeeks(plusWeeks).dayOfWeek().withMinimumValue();
            holidays.put(targetDate, happyMondayHoliday);
        }
    }

    private void pickupSeasonalHolidays(final LocalDate month, final Map<LocalDate, Holiday> holidays) {
        final List<SeasonalHoliday> seasonalHolidays = this.seasonalHolidaysMapping.get(month);
        if (seasonalHolidays == null) {
            return;
        }
        for (final SeasonalHoliday seasonalHoliday : seasonalHolidays) {
            LocalDate targetDate = seasonalHoliday.getTargetDate();
            if (targetDate.getDayOfWeek() == DateTimeConstants.SUNDAY) {
                targetDate = targetDate.plusDays(1);
            }
            holidays.put(targetDate, seasonalHoliday);
        }
    }

}
