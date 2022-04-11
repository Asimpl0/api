package com.smartcampus.api;

import com.alibaba.fastjson.JSONObject;
import com.smartcampus.dao.GradeDao;
import com.smartcampus.dao.LoginDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GradeServlet", value = "/grade")
public class GradeServlet extends HttpServlet {
    private GradeDao gradeDao;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        gradeDao = new GradeDao(jdbcTemplate);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        int year =Integer.valueOf(request.getParameter("year"));
        int semester = Integer.valueOf(request.getParameter("semester"));
        System.out.println(sid + " " + year + " " + semester);
        gradeDao.getGrade(sid, year,semester);
//        JSONObject o = new JSONObject();
//
//        response.getWriter().println(o.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
