package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Forum.ForumDao;
import com.smartcampus.dao.Forum.PostInfo;
import com.smartcampus.dao.Posting.CommentInfo;
import com.smartcampus.dao.Posting.PostingDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PostingServlet", value = "/posting")
public class PostingServlet extends HttpServlet {
    private PostingDao postingDao;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        postingDao = new PostingDao(jdbcTemplate);
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
        String pid = request.getParameter("pid");
        switch (funct){
            case "0":{
                PostInfo postInfo = postingDao.getPost(pid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(postInfo).toString());
                break;
            }
            case "1":{
                List<CommentInfo> list = postingDao.getComment(pid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        String pid = request.getParameter("pid");
        String comment = request.getParameter("comment");
        postingDao.doComment(sid,pid,comment);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("success");
    }
}
