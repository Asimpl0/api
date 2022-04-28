package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Book.*;
import com.smartcampus.dao.Grade.GradeDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookServlet", value = "/book")
public class BookServlet extends HttpServlet {
    private BookDao bookDao;
    private BookRecommend recommend;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        bookDao = new BookDao(jdbcTemplate);
        recommend = new BookRecommend(jdbcTemplate);
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
        switch (funct) {
            case "0": {
                //搜索书籍
                String book = request.getParameter("book");
                String status = request.getParameter("status");
                String type = request.getParameter("type");
                Book result = bookDao.getBook(book, status, type);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(result).toString());
                break;
            }
            case "1" :{
                //借阅书籍
                String bid = request.getParameter("bid");
                String days = request.getParameter("days");
                System.out.printf(bid + days);
                bookDao.borrowBook(sid,bid,days);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "2" :{
                //查看借书记录
                List<Mybook> list = bookDao.getMybook(sid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "3" :{
                //书籍评分
                String boid = request.getParameter("boid");
                String rate = request.getParameter("rate");
                bookDao.RateBook(boid,rate);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "4": {
                //基于用户的推荐
                List<BookInfo> list = recommend.RecommendBasedUser(sid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "5": {
                //基于物品的推荐
                String bid = request.getParameter("bid");
                System.out.println(bid);
                List<BookInfo> list = recommend.RecommendBasedItem(bid);
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
