package com.github.kazuki43zoo.web.mvc;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.terasoluna.gfw.common.date.DateFactory;

import com.github.kazuki43zoo.core.config.ApplicationConfigs;
import com.github.kazuki43zoo.core.config.SecurityConfigs;

@ControllerAdvice
@lombok.RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GlobalControllerAdvice {

    private final @lombok.NonNull SecurityConfigs securityConfigs;

    private final @lombok.NonNull ApplicationConfigs applicationConfigs;

    private final @lombok.NonNull DateFactory dateFactory;

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
