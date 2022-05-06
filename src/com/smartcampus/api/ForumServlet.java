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
        String funct = request.getParameter("funct");
        switch (funct){
            case "0":{
                //获得所有帖子
                String block = request.getParameter("block");
                String sort = request.getParameter("sort");
                String add = request.getParameter("add");
                String search = request.getParameter("search");
                if (search.isEmpty())
                    search = "";
                List<PostInfo> list = forumDao.getPost(block,sort,add,sid,search);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "1":{
                //对帖子进行点赞
                String pid = request.getParameter("pid");
                String rate = request.getParameter("do");
                forumDao.doLike(pid,rate);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "2":{
                //对帖子进行收藏
                String pid = request.getParameter("pid");
                String exe = request.getParameter("do");
                forumDao.doCollect(sid,pid,exe);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "3":{
                //获得排行
                String block = request.getParameter("block");
                String sort = request.getParameter("sort");
                System.out.println(block);
                List<String> list = forumDao.getRank(block,sort);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "4":{
                //获得点赞数
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(forumDao.getLikes(sid)).toString());
                break;
            }
            case "5":{
                //获得所有食堂或者选修课
                String block = request.getParameter("block");
                System.out.println(block);
                List<String> list = forumDao.getAll(block);
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
