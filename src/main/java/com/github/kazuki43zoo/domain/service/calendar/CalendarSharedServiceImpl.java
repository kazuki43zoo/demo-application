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
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public final class CalendarSharedServiceImpl implements CalendarSharedService {

    @lombok.NonNull
    private final FixedHolidayRepository fixedHolidayRepository;

    @lombok.NonNull
    private final HappyMondayHolidayRepository happyMondayHolidayRepository;

    @lombok.NonNull
    private final SeasonalHolidayRepository seasonalHolidayRepository;

    private Map<Integer, List<FixedHoliday>> fixedHolodaiesMapping;

    private Map<Integer, List<HappyMondayHoliday>> happyMondayHolidaiesMapping;

    private Map<LocalDate, List<SeasonalHoliday>> seasonalHolidaiesMapping;

    @PostConstruct
    public void loadHolidaies() {
        loadFixedHolodaies();
        loadHappyMandayHolidaies();
        loadSeasonalHolidaies();
    }

    private void loadFixedHolodaies() {
        Map<Integer, List<FixedHoliday>> fixedHolodaiesMapping = new HashMap<>();
        List<FixedHoliday> fixedHolodaies = fixedHolidayRepository.findAll();
        for (FixedHoliday fixedHoliday : fixedHolodaies) {
            List<FixedHoliday> fixedHolodaiesOfMonth = fixedHolodaiesMapping.get(fixedHoliday
                    .getTargetMonth());
            if (fixedHolodaiesOfMonth == null) {
                fixedHolodaiesOfMonth = new ArrayList<>();
                fixedHolodaiesMapping.put(fixedHoliday.getTargetMonth(), fixedHolodaiesOfMonth);
            }
            fixedHolodaiesOfMonth.add(fixedHoliday);
        }
        this.fixedHolodaiesMapping = Collections.unmodifiableMap(fixedHolodaiesMapping);
    }

    private void loadHappyMandayHolidaies() {
        Map<Integer, List<HappyMondayHoliday>> happyMondayHolidaiesMapping = new HashMap<>();
        List<HappyMondayHoliday> happyMondayHolidaies = happyMondayHolidayRepository.findAll();
        for (HappyMondayHoliday happyMondayHoliday : happyMondayHolidaies) {
            List<HappyMondayHoliday> happyMondayHolidaiesOfMonth = happyMondayHolidaiesMapping
                    .get(happyMondayHoliday.getTargetMonth());
            if (happyMondayHolidaiesOfMonth == null) {
                happyMondayHolidaiesOfMonth = new ArrayList<>();
                happyMondayHolidaiesMapping.put(happyMondayHoliday.getTargetMonth(),
                        happyMondayHolidaiesOfMonth);
            }
            happyMondayHolidaiesOfMonth.add(happyMondayHoliday);
        }
        this.happyMondayHolidaiesMapping = Collections.unmodifiableMap(happyMondayHolidaiesMapping);
    }

    private void loadSeasonalHolidaies() {
        Map<LocalDate, List<SeasonalHoliday>> seasonalHolidaiesMapping = new HashMap<>();
        List<SeasonalHoliday> seasonalHolidaies = seasonalHolidayRepository.findAll();
        for (SeasonalHoliday seasonalHoliday : seasonalHolidaies) {
            LocalDate month = seasonalHoliday.getTargetDate().dayOfMonth().withMinimumValue();
            List<SeasonalHoliday> seasonalHolidaiesOfMonth = seasonalHolidaiesMapping.get(month);
            if (seasonalHolidaiesOfMonth == null) {
                seasonalHolidaiesOfMonth = new ArrayList<>();
                seasonalHolidaiesMapping.put(month, seasonalHolidaiesOfMonth);
            }
            seasonalHolidaiesOfMonth.add(seasonalHoliday);
        }
        this.seasonalHolidaiesMapping = Collections.unmodifiableMap(seasonalHolidaiesMapping);
    }

    @Override
    public Map<LocalDate, Holiday> getHolodaies(LocalDate month) {
        Map<LocalDate, Holiday> holidaies = new LinkedHashMap<>();
        pickupFixedHolidaies(month, holidaies);
        pickupHappyMondayHolidaies(month, holidaies);
        pickupSeasonalHolidaies(month, holidaies);
        return holidaies;
    }

    private void pickupFixedHolidaies(LocalDate month, Map<LocalDate, Holiday> holidaies) {
        List<FixedHoliday> fixedHolidaies = fixedHolodaiesMapping.get(month.getMonthOfYear());
        if (fixedHolidaies == null) {
            return;
        }
        for (FixedHoliday fixedHoliday : fixedHolidaies) {
            if (!((fixedHoliday.getBeginYear() <= month.getYear()) && (month.getYear() <= fixedHoliday
                    .getEndYear()))) {
                continue;
            }
            LocalDate targetDate = month.withDayOfMonth(fixedHoliday.getTargetDay());
            if (targetDate.getDayOfWeek() == DateTimeConstants.SUNDAY) {
                targetDate = targetDate.plusDays(fixedHoliday.getTransferredLaterDays());
            }
            holidaies.put(targetDate, fixedHoliday);
        }
    }

    private void pickupHappyMondayHolidaies(LocalDate month, Map<LocalDate, Holiday> holidaies) {
        List<HappyMondayHoliday> happyMondayHolidaies = happyMondayHolidaiesMapping.get(month
                .getMonthOfYear());
        if (happyMondayHolidaies == null) {
            return;
        }
        for (HappyMondayHoliday happyMondayHoliday : happyMondayHolidaies) {
            if (!((happyMondayHoliday.getBeginYear() <= month.getYear()) && (month.getYear() <= happyMondayHoliday
                    .getEndYear()))) {
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

    private void pickupSeasonalHolidaies(LocalDate month, Map<LocalDate, Holiday> holidaies) {
        List<SeasonalHoliday> seasonalHolidaies = seasonalHolidaiesMapping.get(month);
        if (seasonalHolidaies == null) {
            return;
        }
        for (SeasonalHoliday seasonalHoliday : seasonalHolidaies) {
            holidaies.put(seasonalHoliday.getTargetDate(), seasonalHoliday);
        }
    }

}
