package com.smartcampus.dao.Book;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class BookInfoMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBid(resultSet.getInt("bid"));
        bookInfo.setBname(resultSet.getString("bname"));
        bookInfo.setBauthor(resultSet.getString("bauthor"));
        bookInfo.setBpublisher(resultSet.getString("bpublisher"));
        bookInfo.setBtype(resultSet.getString("btype"));
        bookInfo.setBnum(resultSet.getInt("bnum"));
        return bookInfo;
    }
}

class BookTypeMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        BookType bookType = new BookType();
        bookType.setBtype(resultSet.getString("btype"));
        return bookType;
    }
}

class MyBookMapper implements RowMapper{
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Mybook mybook = new Mybook();
        mybook.setBoid(resultSet.getInt("boid"));
        mybook.setBname(resultSet.getString("bname"));
        mybook.setBsdate(String.valueOf(resultSet.getDate("bsdate")));
        mybook.setBedate(String.valueOf(resultSet.getDate("bedate")));
        mybook.setBrate(String.valueOf(resultSet.getInt("brate")));
        return mybook;
    }
}
public class BookDao {
    private JdbcTemplate jdbcTemplate;

    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BookDao() {
    }

    public Book getBook(String name, String status, String type) {
        String sql1 = "SELECT bid,bname,bauthor,bpublisher,btype,bnum FROM book WHERE bname LIKE '%" + name + "%'";
        String sql2 = "SELECT DISTINCT btype FROM book WHERE bname LIKE '%" + name + "%'";
        //status为1即代表查询可借阅的书，判断bnum是否大于0即可
        if ("1".equals(status)) {
            sql1 += "AND bnum > 0";
            sql2 += "AND bnum > 0";
        }

        //status为1即代表查询可借阅的书，判断bnum是否大于0即可
        if ("2".equals(status)) {
            sql1 += "AND bnum = 0";
            sql2 += "AND bnum = 0";
        }

        if (!"全部类型".equals(type)){
            sql1 += "AND btype like " + type;
            sql2 += "AND btype like " + type;
        }
        System.out.println(sql1);
        List<BookType> list1 = jdbcTemplate.query(sql2, new BookTypeMapper());
        List<BookInfo> list2 = jdbcTemplate.query(sql1, new BookInfoMapper());
        return new Book(list1, list2);
    }
    public void borrowBook(String sid, String bid, String days){
        String sql1 = "INSERT INTO borrow (bid, sid, bsdate, bdays) VALUES (?, ?, curdate(), ?);";
        String sql2 = "UPDATE book SET bnum=bnum-1 WHERE bid = "+ bid + " AND bnum > 0";
        System.out.println(sql2);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update(sql1, bid, sid, days);
    }
    public List getMybook(String sid){
        String sql = "SELECT boid, bname, bsdate, bedate, brate  FROM borrow, book " +
                "WHERE borrow.sid = '"+ sid +"' AND borrow.bid = book.bid";
        System.out.println(sql);
        List<Mybook> list = jdbcTemplate.query(sql,new MyBookMapper());
        return list;
    }
    public void RateBook(String boid, String rate){
        String sql = "UPDATE borrow SET brate=" + rate + " WHERE boid = "+ boid;
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }
}
