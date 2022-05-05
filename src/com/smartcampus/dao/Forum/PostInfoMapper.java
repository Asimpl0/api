package com.smartcampus.dao.Forum;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        PostInfo postInfo = new PostInfo();
        postInfo.setNickName(resultSet.getString("nickName"));
        postInfo.setAvatarUrl(resultSet.getString("avatarUrl"));
        postInfo.setPtime(resultSet.getString("ptime"));
        postInfo.setPtag(resultSet.getInt("ptag"));
        postInfo.setCnums(resultSet.getInt("cnums"));
        postInfo.setInums(resultSet.getInt("lnums"));
        postInfo.setConums(resultSet.getInt("conums"));
        postInfo.setPdetail(resultSet.getString("pdetail"));
        postInfo.setPid(resultSet.getInt("pid"));
        postInfo.setIsmark(resultSet.getInt("ismark"));
        postInfo.setMark(resultSet.getInt("mark"));
        postInfo.setName(resultSet.getString("name"));
        return postInfo;
    }
}
