package com.smartcampus.dao.Course;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setCid(resultSet.getString("Cid"));
        teacherInfo.setTname(resultSet.getString("Tname"));
        teacherInfo.setRoom(resultSet.getString("Room"));
        return teacherInfo;
    }
}
