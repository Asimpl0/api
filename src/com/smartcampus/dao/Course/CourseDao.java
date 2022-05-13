package com.smartcampus.dao.Course;

import com.smartcampus.dao.Posting.CommentInfoMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class CourseMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Course course = new Course();
        course.setCname(resultSet.getString("Cname"));
        course.setTname(resultSet.getString("Tname"));
        course.setWbegin(resultSet.getInt("Wbegin"));
        course.setWeeks(resultSet.getInt("Weeks"));
        course.setRoom(resultSet.getString("Room"));
        course.setNums(resultSet.getInt("Nums"));
        course.setTdetail(resultSet.getString("Tdetail").split(" "));
        return course;
    }
}

public class CourseDao {
    private JdbcTemplate jdbcTemplate;

    public CourseDao() {
    }

    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Course> getCourse(String sid) {
        String sql;
        sql = "SELECT Cname, Tname, Room, Weeks, Wbegin, Nums, Tdetail\n" +
                "FROM s_c,course,teacher,t_c\n" +
                "WHERE s_c.Sid = '" + sid + "'" + " AND s_c.Cid = course.Cid AND s_c.Cid = t_c.Cid AND t_c.Tid = teacher.Tid AND s_c.Class = t_c.Class;";
        List<Course> list = jdbcTemplate.query(sql, new CourseMapper());
        return list;
    }

    public List<CourseInfo> searchCourse(String search) {
        String sql1 = "SELECT Cid,Cname,Type,Credit FROM `course`\n" +
                "WHERE Cname like '%" + search + "%'";
        System.out.println(sql1);
        List<CourseInfo> list = jdbcTemplate.query(sql1, new CourseInfoMapper());
        System.out.println(list);
        String sql2 = "SELECT Cid,Tname,Room FROM t_c,teacher\n" +
                "WHERE t_c.Tid = teacher.tid";
        List<TeacherInfo> teachers = jdbcTemplate.query(sql2, new TeacherInfoMapper());
        for (TeacherInfo teacher : teachers) {
            for (CourseInfo courseInfo : list) {
                if (courseInfo.cid.equals(teacher.cid))
                    courseInfo.teachers.add(teacher);
            }
        }
        return list;
    }
}
