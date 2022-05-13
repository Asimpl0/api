package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.Login.LoginDao;
import com.smartcampus.utils.Rsa;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Base64;

@WebServlet(name = "loginServlet", value = "/login")
public class loginServlet extends HttpServlet {

    private LoginDao loginDao;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        loginDao = new LoginDao(jdbcTemplate);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获得用户名
        String Uid = request.getParameter("uid");
        //获得密码
        String Passwd = request.getParameter("passwd");
//        System.out.println(Uid);
//        System.out.println(Passwd);
        String uid = null;
        String passwd = null;
        try {
            uid = Rsa.decrypt(Uid, Rsa.getPrivateKey(Rsa.privateKey));
            passwd = Rsa.decrypt(Passwd, Rsa.getPrivateKey(Rsa.privateKey));
//            System.out.println(uid);
//            System.out.println(passwd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        System.out.println(uid);
//        System.out.println(passwd);
        if (loginDao.check(uid, passwd))
            response.getWriter().print("success");
        else
            response.getWriter().print("fail");
        Cookie cookie = new Cookie(uid, passwd);//创建一个键值对的cookie对象
        System.out.println(cookie.getName());
        response.addCookie(cookie);//添加到response中
        System.out.println(cookie);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String sid = null;
        //从Cookie中获得学号
        for (Cookie cookie : cookies) {
            sid = cookie.getName();
        }
        String nickName = request.getParameter("nickName");
        String avatarUrl = request.getParameter("avatarUrl");
        loginDao.update(sid, nickName, avatarUrl);
        response.getWriter().println("success");
    }
}
