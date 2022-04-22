package com.smartcampus.dao.Mainten;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class MaintenMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Mainten mainten = new Mainten();
        mainten.setMid(resultSet.getInt("Mid"));
        mainten.setBdate(resultSet.getDate("Bdate"));
        mainten.setEdate(resultSet.getDate("Edate"));
        mainten.setPlace(resultSet.getString("Place"));
        mainten.setDetail(resultSet.getString("Detail"));
        mainten.setStatus(resultSet.getInt("Mstatus"));
        return mainten;
    }
}

public class MaintenDao {
    private JdbcTemplate jdbcTemplate;

    public MaintenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MaintenDao() {
    }

    public void doMainten(String uid, String place, String tele, String detail) {
        String sql = "INSERT INTO maintenance (Bdate, Sid, Place, Number, Detail) VALUES (curdate(), ?, ?, ?, ?);";
        jdbcTemplate.update(sql, uid, place, tele, detail);
    }

    public List<Mainten> getMainten(String sid, String status, String year) {
        String sql = "SELECT Mid, Bdate, Edate, Place, Detail,Mstatus  FROM `maintenance`" +
                "WHERE Sid = '" + sid +"'";
        if(!"2".equals(status))
            sql += " AND Mstatus = " + status;
        if (!"全部时间".equals(year))
            sql += " AND Bdate like '" + year + "%'";
        System.out.println(sql);
        List<Mainten> list = jdbcTemplate.query(sql,new MaintenMapper());
        System.out.println(list);
        return list;
    }
}
