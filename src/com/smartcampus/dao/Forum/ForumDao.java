package com.smartcampus.dao.Forum;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ForumDao {
    private JdbcTemplate jdbcTemplate;

    public ForumDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ForumDao() {
    }

    public List getPost(String block, String sort, String add, String sid, String search) {
        String[] sorts = {"ptime", "sums"};
        int bindex = Integer.valueOf(block);
        int sindex = Integer.valueOf(sort);
        String sql = "SELECT nickName, avatarUrl,pdetail,pid,ptime,ptag,cnums,lnums,conums,(cnums+lnums+conums) AS sums, ismark,mark,name \n" +
                "FROM login, post WHERE login.uid = post.uid AND (pdetail like '%" + search + "%' OR name like'%" + search + "%')";
        if ("0".equals(add)) //显示我的帖子
            sql += " AND post.uid = '" + sid + "'";
        if ("1".equals(add)) //显示我的收藏
            sql += " AND post.pid IN (\n" +
                    "SELECT pid FROM collect\n" +
                    "WHERE Uid = '" + sid + "'\n" +
                    ")";
        if (bindex != 0) {
            //为0表示显示所有板块
            sql += " AND ptag = " + (bindex - 1);
        }
        sql += "\nORDER BY " + sorts[sindex] + " DESC\n";
        sql += "LIMIT 100";
        System.out.println(sql);
        List<PostInfo> list = jdbcTemplate.query(sql, new PostInfoMapper());
        return list;
    }

    public void doLike(String pid, String rate) {
        String sql;
        if ("1".equals(rate)) {
            sql = "UPDATE post\n" +
                    "SET lnums = lnums + 1\n" +
                    "WHERE pid = ?";
        } else
            sql = "UPDATE post\n" +
                    "SET lnums = lnums - 1\n" +
                    "WHERE pid = ?";
        jdbcTemplate.update(sql, pid);
    }

    public void doCollect(String sid, String pid, String exe) {
        String sql1, sql2;
        if ("1".equals(exe)) {
            //收藏操作
            sql1 = "INSERT INTO collect (uid, pid)\n" +
                    "VALUES(?,?)\n";
            sql2 = "UPDATE post\n" +
                    "SET conums = conums + 1\n" +
                    "WHERE pid = ?";
        } else {
            sql1 = "DELETE FROM collect\n" +
                    "WHERE Uid = ? AND pid = ?";
            sql2 = "UPDATE post\n" +
                    "SET conums = conums - 1\n" +
                    "WHERE pid = ?";
        }


        jdbcTemplate.update(sql1, sid, pid);
        jdbcTemplate.update(sql2, pid);
    }

    public List getSearch(String search) {
        String sql = "SELECT nickName, avatarUrl,pdetail,pid,ptime,ptag,cnums,lnums,conums,(cnums+lnums+conums) AS sums, ismark,mark,name \n" +
                "FROM login, post WHERE login.uid = post.uid AND pdetail like '%" + search + "%' OR name like'%" + search + "%'" + " \nORDER BY ptime DESC\n";
        sql += "LIMIT 100";
        System.out.println(sql);
        List<PostInfo> list = jdbcTemplate.query(sql, new PostInfoMapper());
        return list;
    }

    public int getLikes(String sid) {
        String sql = "SELECT SUM(lnums) FROM post\n" +
                "WHERE uid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, sid);
    }

    public List getRank(String block, String sort) {
        String sql = null;
        if ("5".equals(block)) {
            //食堂高分排行
            if ("0".equals(sort))
                sql = "SELECT name, SUM(mark)/COUNT(*) AS num FROM `post`\n" +
                        "WHERE ptag = 4 \n" +
                        "GROUP BY name\n" +
                        "ORDER BY num DESC\n" +
                        "LIMIT 3";
            else
                //食堂热度排行，根据发帖次数
                sql = "SELECT name, COUNT(*) AS num FROM `post`\n" +
                        "WHERE ptag = 4 \n" +
                        "GROUP BY name\n" +
                        "ORDER BY num DESC\n" +
                        "LIMIT 3";
        }
        if ("4".equals(block)){
            //选修课高分排行
            if ("0".equals(sort))
                sql = "SELECT name, SUM(mark)/COUNT(*) AS num FROM `post`\n" +
                        "WHERE ptag = 3 \n" +
                        "GROUP BY name\n" +
                        "ORDER BY num DESC\n" +
                        "LIMIT 3";
            else
                //选修课热度排行，根据发帖次数
                sql = "SELECT name, COUNT(*) AS num FROM `post`\n" +
                        "WHERE ptag = 3 \n" +
                        "GROUP BY name\n" +
                        "ORDER BY num DESC\n" +
                        "LIMIT 3";
        }
        System.out.println(sql);
        List<RankInfo> list = jdbcTemplate.query(sql, new RankInfoMapper());
        return list;
    }

    public List getAll(String block){
        String sql = null;
        if ("4".equals(block)){
            //为选修课
            sql = "SELECT Sename AS name FROM `selective`\n" +
                    "ORDER BY Sename";
        }
        if ("5".equals(block))
            sql = "SELECT Caname AS name FROM `canteen`\n" +
                    "ORDER BY Caname";
        List<String> list = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("name");
            }
        });
        return list;
    }
}

