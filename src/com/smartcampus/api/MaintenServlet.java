package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.smartcampus.dao.Mainten.Mainten;
import com.smartcampus.dao.Mainten.MaintenDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MaintenServlet", value = "/mainten")
public class MaintenServlet extends HttpServlet {
    private MaintenDao maintenDao;
    private JdbcTemplate jdbcTemplate;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        maintenDao = new MaintenDao(jdbcTemplate);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String funct = request.getParameter("funct");
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        System.out.println(funct);
        switch (funct) {
            //0表示获得报修地址选项
            case "0": {
                Map<String, String[]> map = new HashMap<>();
                String[] s1 = {"东一", "东二", "东三", "东四", "东五"};
                String[] s2 = {"西一", "西二", "西三", "西四", "西五"};
                String[] s3 = {"南一", "南二", "南三", "南四", "南五"};
                map.put("东校区", s1);
                map.put("西校区", s2);
                map.put("南校区", s3);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(JSON.toJSON(map).toString());
                break;
            }
            //1表示按处理状态查询报修
            case "1": {
                String status = request.getParameter("status");
                String year = request.getParameter("year");
                System.out.println(year);
                List<Mainten> list = maintenDao.getMainten( sid, status, year);
                System.out.println(list);
                response.setCharacterEncoding("UTF-8");
                System.out.println(JSON.toJSONString(list,SerializerFeature.WriteMapNullValue));
                response.getWriter().println(JSON.toJSONString(list,SerializerFeature.WriteMapNullValue));
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
        String tele = request.getParameter("tele");
        String place = request.getParameter("place");
        String detail = request.getParameter("detail");
        System.out.println(detail);
        maintenDao.doMainten(sid, place, tele, detail);
        response.getWriter().println("OK");
    }
}
