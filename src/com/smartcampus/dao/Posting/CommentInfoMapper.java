package com.smartcampus.dao.Posting;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setAvatarUrl(resultSet.getString("avatarUrl"));
        commentInfo.setNickName(resultSet.getString("nickName"));
        commentInfo.setPid(resultSet.getString("cid"));
        commentInfo.setCtime(resultSet.getString("ctime"));
        commentInfo.setCdetail(resultSet.getString("cdetail"));
        return commentInfo;
    }
}
