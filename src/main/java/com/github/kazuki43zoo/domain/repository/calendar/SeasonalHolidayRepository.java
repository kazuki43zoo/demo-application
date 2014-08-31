package com.github.kazuki43zoo.domain.repository.calendar;

import java.util.List;

import com.github.kazuki43zoo.domain.model.calendar.SeasonalHoliday;

public interface SeasonalHolidayRepository {
    List<SeasonalHoliday> findAll();
}
