package com.github.kazuki43zoo.domain.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountPasswordLock implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private int failureCount;
    private Date modifiedAt;

    public void countUpFailureCount() {
        failureCount++;
    }

}
