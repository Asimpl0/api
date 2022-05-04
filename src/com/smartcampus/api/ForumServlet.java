package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Forum.ForumDao;
import com.smartcampus.dao.Forum.PostInfo;
import com.smartcampus.dao.Post.PostDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ForumServlet", value = "/forum")
public class ForumServlet extends HttpServlet {
    private ForumDao forumDao;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        forumDao = new ForumDao(jdbcTemplate);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        String block = request.getParameter("block");
        String sort = request.getParameter("sort");
        List<PostInfo> list = forumDao.getPost(block,sort);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JSON.toJSON(list).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
