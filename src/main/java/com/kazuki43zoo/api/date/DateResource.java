package com.kazuki43zoo.api.date;

import org.joda.time.DateTime;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@lombok.Data
@lombok.EqualsAndHashCode(callSuper = true)
@lombok.ToString(callSuper = true)
public class DateResource extends RepresentationModel<DateResource> implements Serializable {
    private static final long serialVersionUID = 1L;
    private DateTime dateTime;
}
