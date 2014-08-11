package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkPlace implements Serializable {
    private static final long serialVersionUID = 1L;
    private String workPlaceUuid;
    private String workPlaceName;
}
