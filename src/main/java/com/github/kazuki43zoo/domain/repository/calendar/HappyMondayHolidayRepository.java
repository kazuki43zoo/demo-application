package com.github.kazuki43zoo.domain.repository.calendar;

import com.github.kazuki43zoo.domain.model.calendar.HappyMondayHoliday;

import java.util.List;

public interface HappyMondayHolidayRepository {
    List<HappyMondayHoliday> findAll();
}
