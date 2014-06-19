package com.github.kazuki43zoo.app.account;

import java.util.List;

import lombok.Data;

@Data
public class AccountsSearchQuery {

    private String word;
    private List<String> targets;

}
