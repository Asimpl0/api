package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Activity.ActivityDao;
import com.smartcampus.dao.Activity.ActivityRecommendDao;
import com.smartcampus.dao.Post.ChoiceItem;
import com.smartcampus.dao.Post.PostDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PostServlet", value = "/post")
public class PostServlet extends HttpServlet {
    private PostDao postDao;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        postDao = new PostDao(jdbcTemplate);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        List< ChoiceItem> list = postDao.getChoice(sid);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JSON.toJSON(list).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        String type = request.getParameter("type");
        String post = request.getParameter("post");
        System.out.println(post);
        switch (type){
            case "0" : {
                int blockid = Integer.valueOf(request.getParameter("blockid")) ;
                System.out.println(blockid);
                postDao.postNormal(sid,post,blockid-1);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "1": {
                String rate = request.getParameter("rate");
                String blockid = request.getParameter("blockid");
                String itemid = request.getParameter("itemid");
                postDao.postRate(sid,post,blockid,itemid,rate);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
        }
    }
}
