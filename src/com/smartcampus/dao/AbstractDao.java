package com.smartcampus.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class AbstractDao {
    private JdbcTemplate jdbcTemplate;

    public AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AbstractDao() {
    }
}
