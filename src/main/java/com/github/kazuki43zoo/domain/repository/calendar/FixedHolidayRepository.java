package com.github.kazuki43zoo.domain.repository.calendar;

import java.util.List;

import com.github.kazuki43zoo.domain.model.calendar.FixedHoliday;

public interface FixedHolidayRepository {
    List<FixedHoliday> findAll();
}
