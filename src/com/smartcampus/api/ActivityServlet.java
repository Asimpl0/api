package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Activity.ActivityDao;
import com.smartcampus.dao.Activity.ActivityInfo;
import com.smartcampus.dao.Activity.ActivityRecommendDao;
import com.smartcampus.dao.Activity.Engagement;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ActivityServlet", value = "/activity")
public class ActivityServlet extends HttpServlet {
    private ActivityDao activityDao;
    public ActivityRecommendDao activityRecommendDao;

    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        activityDao = new ActivityDao(jdbcTemplate);
        activityRecommendDao = new ActivityRecommendDao(jdbcTemplate);
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
                //获得所有活动
                List<ActivityInfo> list = activityDao.getAllActivity();
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "1": {
                //获得搜索活动
                String search = request.getParameter("search");
                System.out.println(search);
                List<ActivityInfo> list = activityDao.getSearchActivity(search);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "2": {
                //参与活动
                String aid = request.getParameter("aid");
                System.out.println(aid);
                activityDao.engageActivity(sid,aid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "3": {
                //获得参与的活动
                List<Engagement> list = activityDao.getEngagedActivity(sid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "4": {
                //评分
                String eid = request.getParameter("eid");
                String rate = request.getParameter("rate");
                activityDao.rateActivity(eid,rate);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println("success");
                break;
            }
            case "5": {
                //基于用户的协同过滤推荐
                List<ActivityInfo> list = activityRecommendDao.recommendbasedUser(sid);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(list).toString());
                break;
            }
            case "6": {
                //获得当前活动类似活动
                String aid = request.getParameter("aid");
                List<ActivityInfo> list = activityRecommendDao.recommendbasedTheme(aid);
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
