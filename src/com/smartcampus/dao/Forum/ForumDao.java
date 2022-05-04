package com.smartcampus.dao.Forum;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ForumDao {
    private JdbcTemplate jdbcTemplate;

    public ForumDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ForumDao() {
    }

    public List getPost(String block, String sort){
        String[] sorts = {"ptime","lnums"};
        int bindex = Integer.valueOf(block);
        int sindex = Integer.valueOf(sort);
        String sql = "SELECT nickName, avatarUrl,pdetail,pid,ptime,ptag,cnums,lnums,conums\n" +
                "FROM login, post\n" +
                "WHERE login.Uid = post.uid \n";
        if (bindex != 0){
            //为0表示显示所有板块
            sql += "AND ptag = " + (bindex - 1);
        }
        if (sindex == 0){
            sql += "\nORDER BY " + sorts[sindex] + " DESC\n";
        }
        sql += "LIMIT 100";
        System.out.println(sql);
        List<PostInfo> list = jdbcTemplate.query(sql, new PostInfoMapper());
        return list;

    }
}
