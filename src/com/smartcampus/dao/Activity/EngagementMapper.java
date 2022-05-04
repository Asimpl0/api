package com.smartcampus.dao.Activity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EngagementMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Engagement engagement = new Engagement();
        engagement.setEid(resultSet.getString("eid"));
        engagement.setAid(resultSet.getString("aid"));
        engagement.setSid(resultSet.getString("sid"));
        engagement.setErate(resultSet.getString("erate"));
        engagement.setAbtime(resultSet.getString("abtime"));
        engagement.setAname(resultSet.getString("aname"));
        return engagement;
    }
}
