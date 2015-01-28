package com.github.kazuki43zoo.domain.repository.account;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@lombok.Data
public final class AccountsSearchCriteria {

    private static final List<String> DEFAULT_TARGETS = Collections.unmodifiableList(Arrays.asList(
            "accountId", "accountName"));

    private String word;
    private List<String> targets;

    public void determineCriteria() {
        if (CollectionUtils.isEmpty(targets)) {
            setTargets(DEFAULT_TARGETS);
        }
    }
}
