package com.smartcampus.dao.Post;

import com.smartcampus.dao.Activity.ActivityDao;
import com.smartcampus.dao.Book.BookDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.object.SqlCall;

import java.util.ArrayList;
import java.util.List;

public class PostDao {
    private JdbcTemplate jdbcTemplate;

    public PostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PostDao() {
    }

    public List getChoice(String sid) {
        List<ChoiceItem> list = new ArrayList<>();
        //1. 首先活动图书的评价选项，包括图书名称和借书单号
        String sql1 = "SELECT DISTINCT boid AS id, bname AS text FROM borrow, book\n" +
                "WHERE book.bid = borrow.bid AND borrow.sid = ?";
        List<ChildrenItem> booklist = jdbcTemplate.query(sql1, new ChildrenItemMapper(), sid);
        list.add(new ChoiceItem("图书", booklist));
        String sql2 = "SELECT DISTINCT eid AS id, aname AS text FROM engagement, activity\n" +
                "WHERE engagement.aid = activity.aid AND engagement.sid = ?";
        List<ChildrenItem> activitylist = jdbcTemplate.query(sql2, new ChildrenItemMapper(), sid);
        list.add(new ChoiceItem("活动", activitylist));
        String sql3 = "SELECT seid AS id, sename AS text FROM selective";
        List<ChildrenItem> selectivelist = jdbcTemplate.query(sql3, new ChildrenItemMapper());
        list.add(new ChoiceItem("选修课", selectivelist));
        String sql4 = "SELECT caid AS id, caname as text FROM canteen";
        List<ChildrenItem> canteenlist = jdbcTemplate.query(sql4, new ChildrenItemMapper());
        list.add(new ChoiceItem("食堂", canteenlist));
        //System.out.println(list);
        return list;
    }

    public void postNormal(String uid, String post, int blockid) {
        String sql = "INSERT INTO post\n" +
                "(uid,pdetail,ptag)\n" +
                "VALUES(?,?,?)";
        jdbcTemplate.update(sql, uid, post, blockid);
    }

    public void postRate(String sid, String post, String blockid, String itemid, String rate){
        System.out.println(blockid);
        String sql2 = null;
        if("1".equals(blockid)){
            //如果为图书评价，将图书评分加入借书记录表
            BookDao bookDao = new BookDao(jdbcTemplate);
            bookDao.RateBook(itemid,rate);
            System.out.println("加入借书记录");
            //获得图书名称
            sql2 = "SELECT bname AS name FROM borrow , book\n" +
                    "WHERE borrow.bid = book.bid AND borrow.boid = ";
        }
        if("2".equals(blockid)){
            //如果为活动评价，将活动评分加入活动记录
            ActivityDao activityDao = new ActivityDao(jdbcTemplate);
            activityDao.rateActivity(itemid, rate);
            System.out.println("加入活动记录");
            sql2 = "SELECT aname AS name FROM engagement,activity\n" +
                    "WHERE engagement.aid = activity.aid AND engagement.eid = ";
        }
        if ("3".equals(blockid)){
            sql2 = "SELECT Sename AS name FROM selective\n" +
                    "WHERE Seid = ";
        }
        if ("4".equals(blockid)){
            sql2 = "SELECT Caname AS name FROM `canteen`\n" +
                    "WHERE Caid = ";
        }
        sql2 += itemid;
        String name = jdbcTemplate.queryForObject(sql2,String.class);
        String sql1 = "INSERT INTO post\n" +
                "(uid,pdetail,ptag, ismark,mark,mid,name)\n" +
                "VALUES(?,?,?,?,?,?,?)";
        System.out.println(name);
        jdbcTemplate.update(sql1, sid, post, blockid,"1",rate,itemid,name);
    }
}
