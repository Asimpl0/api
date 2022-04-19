package com.smartcampus.api;

import com.alibaba.fastjson.JSON;
import com.smartcampus.dao.LoginDao;
import com.smartcampus.dao.Number;
import com.smartcampus.dao.NumberDao;

import com.smartcampus.utils.PinYin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

class Numbers {
    public String head;
    public List<Number> numbers;

    public Numbers(String head) {
        this.head = head;
        numbers = new ArrayList<>();
    }
    public Numbers(String head, Number number){
        this.head = head;
        numbers = new ArrayList<>();
        numbers.add(number);
    }
}

@WebServlet(name = "NumberServlet", value = "/number")
public class NumberServlet extends HttpServlet {
    private NumberDao numberDao;

    {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("bean.xml");
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        numberDao = new NumberDao(jdbcTemplate);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dname = request.getParameter("dname");
        List<Numbers> list = new ArrayList<>();
        List<Number> numbers = numberDao.getNumber(dname);

        for (Number number : numbers) {
            System.out.println(number.getDname());
            String head = String.valueOf(PinYin.getFullSpell(number.getDname()).charAt(0));
            Numbers tmp = null;
            boolean hasFound = false;
            for (Numbers num : list) {
                if (num.head.equals(head)){
                    num.numbers.add(number);
                    hasFound = true;
                }
            }
            if(!hasFound)
                list.add(new Numbers(head, number));

        }
        Collections.sort(list, new Comparator<Numbers>() {
            @Override
            public int compare(Numbers o1, Numbers o2) {
                return o1.head.compareTo(o2.head);
            }
        });
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(JSON.toJSON(list).toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
