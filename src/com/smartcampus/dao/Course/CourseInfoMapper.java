package com.smartcampus.dao.Course;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setCid(resultSet.getString("Cid"));
        courseInfo.setType(resultSet.getString("Type"));
        courseInfo.setCname(resultSet.getString("Cname"));
        courseInfo.setCredit(resultSet.getString("Credit"));
        //courseInfo.setTeachers();
        return courseInfo;
    }
}
