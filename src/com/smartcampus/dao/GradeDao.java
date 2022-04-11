package com.smartcampus.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class GradeMAapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Grade grade = new Grade();
        grade.setCname(resultSet.getString("Cname"));
        grade.setType(resultSet.getString("Type"));
        grade.setCredit(resultSet.getInt("Credit"));
        grade.setGrade(resultSet.getString("Grade"));
        return grade;
    }
}


public class GradeDao {
    private JdbcTemplate jdbcTemplate;

    public GradeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public GradeDao() {
    }

    public List<Grade> getGrade(String Sid, int year, int semester) {
        //如果semester为 0 需要返回一整个学期的成绩，也即查询年份为 year 学期为2 年份为 year + 1 学期为1
        //如果semester为 1 需要返回整个学年的第一个学期成绩，也即年份为 year 学期为2的结果
        //如果semester为 2 需要返回整个学年的第二个学期成绩，也即年份为year + 1 学期为1的结果
        String sql = null;
        if (semester == 0)
            sql = "SELECT Cname, Type,Credit,Grade FROM s_c,course\n" +
                    "WHERE s_c.Cid = course.Cid AND s_c.Sid = ? AND s_c.Year = ? AND s_c.Semester=?;";
        List<Grade> list = jdbcTemplate.query(sql, new GradeMAapper(), Sid, year, semester);
        System.out.println(list);
        return list;
    }

}
