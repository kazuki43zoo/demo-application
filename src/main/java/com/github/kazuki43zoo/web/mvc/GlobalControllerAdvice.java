package com.github.kazuki43zoo.web.mvc;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.terasoluna.gfw.common.date.DateFactory;

import com.github.kazuki43zoo.core.config.ApplicationConfigs;
import com.github.kazuki43zoo.core.config.SecurityConfigs;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Inject
    SecurityConfigs securityConfigs;

    @Inject
    ApplicationConfigs applicationConfigs;

    @Inject
    DateFactory dateFactory;

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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
