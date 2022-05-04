package com.smartcampus.dao.Post;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ChildrenItemMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        ChildrenItem childrenItem = new ChildrenItem();
        childrenItem.setId(resultSet.getInt("id"));
        childrenItem.setText(resultSet.getString("text"));
        return childrenItem;
    }
}
