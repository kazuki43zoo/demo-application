package com.github.kazuki43zoo.domain.repository.timecard;

import org.apache.ibatis.annotations.Param;

import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;

public interface WorkPlaceRepository {

    WorkPlace findOne(@Param("workPlaceUuid") String workPlaceUuid);

}
