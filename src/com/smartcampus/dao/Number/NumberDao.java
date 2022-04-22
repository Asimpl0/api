package com.smartcampus.dao.Number;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class NumberMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Number number = new Number();
        number.setDname(resultSet.getString("Dname"));
        number.setNumber(resultSet.getString("Number"));

        return number;
    }
}

public class NumberDao {
    private JdbcTemplate jdbcTemplate;

    public NumberDao() {
    }

    public NumberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Number> getNumber(String dname) {
        String sql;
        if ("all".equals(dname))
            sql =  "SELECT * FROM department\n;";
        else
            sql = "SELECT * FROM department\n" +
                    "WHERE Dname LIKE '%" + dname + "%';";
        System.out.println(sql);
        List<Number> list = jdbcTemplate.query(sql, new NumberMapper());
        return list;
    }
}
