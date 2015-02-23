package com.github.kazuki43zoo.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@lombok.Data
public final class ApplicationConfigs {
    @Value("${format.dateTime}")
    private String dateTimeFormat;
    @Value("${format.timeZone}")
    private String timeZoneFormat;
}
