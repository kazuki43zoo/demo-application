package com.github.kazuki43zoo.domain.service.timecard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.exception.SystemException;

import com.github.kazuki43zoo.core.message.Message;
import com.github.kazuki43zoo.domain.model.timecard.WorkPlace;
import com.github.kazuki43zoo.domain.repository.timecard.WorkPlaceRepository;

@Transactional
@Service
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public class WorkPlaceSharedServiceImpl implements WorkPlaceSharedService {

    @Named("workPlaceBatchModeRepository")
    private final @lombok.NonNull WorkPlaceRepository workPlaceRepository;

    private Map<String, WorkPlace> cachedWorkPlaces = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadWorkPlaceOfMainOffice() {
        WorkPlace workPlaceOfMainOffice = getWorkPlace(WorkPlace.MAIN_OFFICE_UUID);
        if (workPlaceOfMainOffice == null) {
            throw new SystemException(Message.FW_DATA_INCONSISTENCIES.code(),
                    "Workplace record of main office has not been inserted. uuid : "
                            + WorkPlace.MAIN_OFFICE_UUID);
        }
    }

    @Override
    public WorkPlace getMainOffice() {
        return getWorkPlace(WorkPlace.MAIN_OFFICE_UUID);
    }

    @Override
    public WorkPlace getWorkPlace(String workPlaceUuid) {
        if (!StringUtils.hasLength(workPlaceUuid)) {
            workPlaceUuid = WorkPlace.MAIN_OFFICE_UUID;
        }
        WorkPlace workPlace = cachedWorkPlaces.get(workPlaceUuid);
        if (workPlace == null) {
            synchronized (workPlaceUuid.intern()) {
                workPlace = cachedWorkPlaces.get(workPlaceUuid);
                if (workPlace == null) {
                    workPlace = workPlaceRepository.findOne(workPlaceUuid);
                    if (workPlace != null) {
                        workPlace.initialize();
                        cachedWorkPlaces.put(workPlaceUuid, workPlace);
                    }
                }
            }
        }
        return workPlace;
    }

    @Override
    public WorkPlace getWorkPlaceDetail(WorkPlace workPlace) {
        if (workPlace == null) {
            return null;
        }
        String workPlaceUuid = workPlace.getWorkPlaceUuid();
        if (StringUtils.hasLength(workPlaceUuid)) {
            return getWorkPlace(workPlaceUuid);
        } else {
            return null;
        }
    }

}
