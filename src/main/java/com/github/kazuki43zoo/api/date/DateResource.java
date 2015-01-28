package com.github.kazuki43zoo.api.date;

import org.joda.time.DateTime;

import java.io.Serializable;

@lombok.Data
public class DateResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private DateTime dateTime;
}
