package com.github.kazuki43zoo.app.account;

import java.io.Serializable;
import java.util.List;

@lombok.Data
public class AccountsSearchQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    private String word;

    private List<String> targets;

}
