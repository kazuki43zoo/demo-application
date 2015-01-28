package com.github.kazuki43zoo.domain.repository.calendar;

import com.github.kazuki43zoo.domain.model.calendar.SeasonalHoliday;

import java.util.List;

public interface SeasonalHolidayRepository {
    List<SeasonalHoliday> findAll();
}
