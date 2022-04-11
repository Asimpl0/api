package com.smartcampus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.PanelUI;
import java.util.List;

@Repository
public class LoginDao extends AbstractDao {

    private JdbcTemplate jdbcTemplate;

    public LoginDao(JdbcTemplate jdbcTemplate, JdbcTemplate jdbcTemplate1) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate1;
    }

    public LoginDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LoginDao() {
    }

    public boolean check(String uid, String passwd) {
        String sql = "SELECT PASSWD FROM `login` WHERE UID= '" + uid +"'" +";";
        System.out.println(sql);
        String uPasswd = jdbcTemplate.queryForObject(sql, String.class);
        System.out.println(uPasswd);
        if (uPasswd == null)
            return false;
        return passwd.equals(uPasswd);
    }
}
