package com.github.kazuki43zoo.domain.repository.calendar;

import java.util.List;

import com.github.kazuki43zoo.domain.model.calendar.HappyMondayHoliday;

public interface HappyMondayHolidayRepository {
    List<HappyMondayHoliday> findAll();
}
