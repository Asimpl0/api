package com.smartcampus.dao.Activity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.setAid(resultSet.getString("aid"));
        activityInfo.setAname(resultSet.getString("aname"));
        activityInfo.setAaddress(resultSet.getString("aaddress"));
        activityInfo.setAbtime(String.valueOf(resultSet.getDate("abtime")));
        activityInfo.setAtheme(resultSet.getString("atheme"));
        return activityInfo;
    }
}
