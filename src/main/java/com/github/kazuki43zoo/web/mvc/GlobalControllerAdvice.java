package com.github.kazuki43zoo.web.mvc;

import com.github.kazuki43zoo.core.config.ApplicationConfigs;
import com.github.kazuki43zoo.core.config.SecurityConfigs;
import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
public final class GlobalControllerAdvice {

    @Inject
    SecurityConfigs securityConfigs;

    @Inject
    ApplicationConfigs applicationConfigs;

    @Inject
    JodaTimeDateFactory dateFactory;

    @ModelAttribute
    public SecurityConfigs setUpSecurityConfigs() {
        return securityConfigs;
    }

    @ModelAttribute
    public ApplicationConfigs setUpApplicationConfigs() {
        return applicationConfigs;
    }

    @ModelAttribute("serverTime")
    public DateTime setUpServerTime() {
        return dateFactory.newDateTime();
    }

    @ModelAttribute("locale")
    public Locale setUpLocale(HttpServletRequest req) {
        return RequestContextUtils.getLocale(req);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
