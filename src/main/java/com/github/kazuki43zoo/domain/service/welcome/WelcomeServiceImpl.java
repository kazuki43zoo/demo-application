package com.github.kazuki43zoo.domain.service.welcome;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.date.DateFactory;

@Service
@Transactional
public class WelcomeServiceImpl implements WelcomeService {

    @Inject
    DateFactory dateFactory;

    @Transactional(readOnly = true)
    public DateTime getCurrentDateTime() {
        return dateFactory.newDateTime();
    }

}
