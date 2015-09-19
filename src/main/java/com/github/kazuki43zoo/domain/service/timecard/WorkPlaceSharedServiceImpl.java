package com.github.kazuki43zoo.domain.service.timecard;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.domain.repository.timecard.WorkPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.exception.SystemException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Transactional
@Service
public final class WorkPlaceSharedServiceImpl implements WorkPlaceSharedService {

    private Map<String, WorkPlace> cachedWorkPlaces = new ConcurrentHashMap<>();

    @Inject
    @Named("workPlaceBatchModeRepository")
    WorkPlaceRepository workPlaceRepository;

    @PostConstruct
    public void loadWorkPlaceOfMainOffice() {
        final WorkPlace workPlaceOfMainOffice = getWorkPlace(WorkPlace.MAIN_OFFICE_UUID);
        if (workPlaceOfMainOffice == null) {
            throw new SystemException(Message.FW_DATA_INCONSISTENCIES.code(), "Workplace record of main office has not been inserted. uuid : " + WorkPlace.MAIN_OFFICE_UUID);
        }
    }

    @Override
    public WorkPlace getMainOffice() {
        return getWorkPlace(WorkPlace.MAIN_OFFICE_UUID);
    }

    @Override
    public WorkPlace getWorkPlace(final String workPlaceUuid) {
        String targetWorkPlaceUuid = workPlaceUuid;
        if (!StringUtils.hasLength(targetWorkPlaceUuid)) {
            targetWorkPlaceUuid = WorkPlace.MAIN_OFFICE_UUID;
        }
        WorkPlace workPlace = cachedWorkPlaces.get(targetWorkPlaceUuid);
        if (workPlace == null) {
            synchronized (targetWorkPlaceUuid.intern()) {
                workPlace = cachedWorkPlaces.get(targetWorkPlaceUuid);
                if (workPlace == null) {
                    workPlace = workPlaceRepository.findOne(targetWorkPlaceUuid);
                    if (workPlace != null) {
                        workPlace.initialize();
                        cachedWorkPlaces.put(targetWorkPlaceUuid, workPlace);
                    }
                }
            }
        }
        return workPlace;
    }

    @Override
    public WorkPlace getWorkPlaceDetail(final WorkPlace workPlace) {
        if (workPlace == null) {
            return null;
        }
        final String workPlaceUuid = workPlace.getWorkPlaceUuid();
        if (StringUtils.hasLength(workPlaceUuid)) {
            return getWorkPlace(workPlaceUuid);
        } else {
            return null;
        }
    }

}
