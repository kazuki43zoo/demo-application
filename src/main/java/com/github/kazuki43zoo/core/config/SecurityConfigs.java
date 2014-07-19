package com.github.kazuki43zoo.core.config;

import javax.inject.Named;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;

@Named
@Data
public class SecurityConfigs {

    @Value("${security.authenticationFailureMaxCount:5}")
    private int authenticationFailureMaxCount;

    @Value("${security.passwordValidDays:90}")
    private int passwordValidDays;

}
