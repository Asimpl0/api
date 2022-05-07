package com.smartcampus.dao.Forum;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RankInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        RankInfo rankInfo = new RankInfo();
        rankInfo.setText(resultSet.getString("name"));
        rankInfo.setNum(resultSet.getDouble("num"));
        return rankInfo;
    }
}
