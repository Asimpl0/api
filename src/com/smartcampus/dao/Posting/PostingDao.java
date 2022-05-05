package com.smartcampus.dao.Posting;

import com.smartcampus.dao.Forum.PostInfo;
import com.smartcampus.dao.Forum.PostInfoMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class PostingDao {
    private JdbcTemplate jdbcTemplate;

    public PostingDao() {
    }

    public PostingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PostInfo getPost(String pid){

        String sql = "SELECT nickName, avatarUrl,pdetail,pid,ptime,ptag,cnums,lnums,conums,ismark,mark,name\n" +
                "FROM login, post\n" +
                "WHERE login.Uid = post.uid AND post.pid = " + pid;
        //System.out.println(sql);
        PostInfo postInfo = (PostInfo) jdbcTemplate.queryForObject(sql,new PostInfoMapper());
        return postInfo;
    }

    public List getComment(String pid){
        String sql = "SELECT nickName, avatarUrl,cdetail,cid,ctime\n" +
                "FROM login, comment\n" +
                "WHERE login.Uid = comment.uid AND comment.pid = " + pid;
        List<CommentInfo> list = jdbcTemplate.query(sql,new CommentInfoMapper());
        return list;
    }
    public void doComment(String sid, String pid, String comment){
        String sql = "INSERT INTO `comment` (Uid,Pid,Cdetail)\n" +
                "VALUES(?,?,?)";
        jdbcTemplate.update(sql, sid, pid, comment);
    }
}
