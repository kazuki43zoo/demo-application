package com.kazuki43zoo.domain.repository.calendar;

import com.kazuki43zoo.domain.model.calendar.FixedHoliday;

import java.util.List;

public interface FixedHolidayRepository {
    List<FixedHoliday> findAll();
}
