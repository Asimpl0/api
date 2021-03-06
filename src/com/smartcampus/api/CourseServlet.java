package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Course.Course;
import com.smartcampus.dao.Course.CourseDao;
import com.smartcampus.dao.Course.CourseInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CourseServlet", value = "/course")
public class CourseServlet extends HttpServlet {
    private CourseDao courseDao;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        courseDao = new CourseDao(jdbcTemplate);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        String funct = request.getParameter("funct");
        switch (funct){
            case "0":{
                List<Course> list = courseDao.getCourse(sid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "1":{
                String search = request.getParameter("search");
                List<CourseInfo> list = courseDao.searchCourse(search);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }

        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
