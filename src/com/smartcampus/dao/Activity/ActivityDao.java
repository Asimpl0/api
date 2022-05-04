package com.smartcampus.dao.Activity;

import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ActivityDao {
    private JdbcTemplate jdbcTemplate;

    public ActivityDao() {
    }

    public ActivityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获得所有活动信息
     *
     * @return
     */
    public List<ActivityInfo> getAllActivity() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(date));
        String sql = "SELECT * FROM `activity` WHERE abtime > ? ORDER BY abtime ASC";
        List<ActivityInfo> list = jdbcTemplate.query(sql, new ActivityInfoMapper(),formatter.format(date));
        return list;
    }

    public List<ActivityInfo> getSearchActivity(String search) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(date));
        String sql = "SELECT * FROM `activity`\n" +
                "WHERE aname like '%" + search + "%' AND abtime > ? ORDER BY abtime ASC";
        List<ActivityInfo> list = jdbcTemplate.query(sql, new ActivityInfoMapper(),formatter.format(date));
        return list;
    }

    public void engageActivity(String sid, String aid) {
        String sql = "INSERT INTO engagement (sid, aid) VALUES (?,?)";
        jdbcTemplate.update(sql, sid, aid);
    }

    public List<Engagement> getEngagedActivity(String sid){
        String sql = "SELECT aname, eid,engagement.aid AS aid,sid,IFNULL(erate,'未评价')AS erate, abtime FROM engagement, activity\n" +
                "WHERE engagement.aid = activity.aid AND sid = ? ORDER BY abtime ASC";
        List<Engagement> list = jdbcTemplate.query(sql, new EngagementMapper(),sid);
        return list;
    }

    public void rateActivity(String eid, String rate){
        String sql = "UPDATE engagement SET erate = ?\n" +
                "WHERE eid = ?";
        jdbcTemplate.update(sql,rate,eid);
    }

}
