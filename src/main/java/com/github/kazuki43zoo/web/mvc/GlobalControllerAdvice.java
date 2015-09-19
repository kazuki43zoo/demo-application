package com.github.kazuki43zoo.web.mvc;

import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import javax.inject.Inject;
import java.util.Locale;

@ControllerAdvice
public final class GlobalControllerAdvice {

    @Inject
    JodaTimeDateFactory dateFactory;

    @ModelAttribute("serverTime")
    public DateTime setUpServerTime() {
        return dateFactory.newDateTime();
    }

    @ModelAttribute
    public Locale setUpLocale(final Locale locale) {
        return locale;
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
