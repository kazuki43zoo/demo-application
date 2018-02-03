package com.kazuki43zoo.web.mvc;

import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.terasoluna.gfw.common.date.jodatime.JodaTimeDateFactory;

import java.util.Locale;

@ControllerAdvice
@lombok.RequiredArgsConstructor
public final class GlobalControllerAdvice {

    private final JodaTimeDateFactory dateFactory;

    @ModelAttribute("serverTime")
    public DateTime setUpServerTime() {
        return this.dateFactory.newDateTime();
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
