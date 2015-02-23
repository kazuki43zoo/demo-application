package com.github.kazuki43zoo.api.date;

import org.joda.time.DateTime;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.ToString(callSuper = true)
public class DateResource extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 1L;
    private DateTime dateTime;
}
