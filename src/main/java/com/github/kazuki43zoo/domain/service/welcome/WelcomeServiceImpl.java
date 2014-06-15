package com.github.kazuki43zoo.domain.service.welcome;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WelcomeServiceImpl implements WelcomeService {
    @Inject
    @Named("jdbcOperations")
    NamedParameterJdbcOperations jdbcOperations;

    @Transactional(readOnly = true)
    public Date getCurrentDate() {
        return jdbcOperations.queryForObject("SELECT CURRENT_TIMESTAMP",
                EmptySqlParameterSource.INSTANCE, Timestamp.class);

    }

}
