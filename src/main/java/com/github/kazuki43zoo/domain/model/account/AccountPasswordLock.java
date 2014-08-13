package com.github.kazuki43zoo.domain.model.account;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountPasswordLock implements Serializable {

    private static final long serialVersionUID = 1L;
    private String accountUuid;
    private int failureCount;
    private DateTime modifiedAt;

    public void countUpFailureCount() {
        failureCount++;
    }

}
