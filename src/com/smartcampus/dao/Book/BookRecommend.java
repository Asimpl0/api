package com.smartcampus.dao.Book;

import com.smartcampus.utils.Pearson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;



public class BookRecommend {
    private JdbcTemplate jdbcTemplate;
    private List<String> books;
    private int k = 3;

    public BookRecommend() {
    }

    public BookRecommend(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.books = new ArrayList<>();
    }

    public List<BookInfo> RecommendBasedUser(String sid) {
        /**
         * 基于用户的协同过滤算法
         * 对于当前需要推荐的用户，获取其对所有作品对的评分，存入List，再依次获取其他用户的评分，存入List
         * 分别计算出当前用户和其他用户的相似度，再利用大根堆，找出相似度最大的用户
         * 再将这些用户的书推荐给该用户
         */

        //1.获得所有书，将其bid存入数组
        String sql1 = "SELECT\n" +
                "\tbid \n" +
                "FROM\n" +
                "\t`book` \n" +
                "ORDER BY\n" +
                "\tbid ASC";
        books = jdbcTemplate.query(sql1, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("bid");
            }
        });
        //System.out.println(books);
        //2.获得出当前用户外的所有借过书的用户名，将sid存入数组
        String sql2 = "SELECT DISTINCT sid FROM `borrow`\n" +
                "WHERE sid != ?\n" +
                "ORDER BY sid";
        List<String> users = new ArrayList<>();
        users = jdbcTemplate.query(sql2, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("sid");
            }
        }, sid);
        //System.out.println(users);
        //3.开始为所有用户获得评分列表，按books的顺序，存放入list，首先获得当前用户的评分
        List<Double> curMark = getBookMark(sid);        //当前用户的评分列表
        //开始获得其他用户的评分列表，并计算相似度

        PriorityQueue<Similarity> queue = new PriorityQueue<>(new Comparator<Similarity>() {
            @Override
            public int compare(Similarity o1, Similarity o2) {
                return (o1.similarity - o2.similarity)<0?-1:1;
            }
        });
        for (String user : users) {
            List<Double> mark = getBookMark(user);      //获得用户评分表
            //4.分别计算当前用户和其他用户的相似度
            //利用小根堆来得到相似度最高的前3个用户
            Double similarity = Pearson.getPearsonBydim(curMark, mark);
            //如果小根堆未满
            //System.out.println(similarity);
            if (queue.size() < k)
                queue.offer(new Similarity(user, similarity));
            else {
                //小根堆满了
                if(similarity > queue.peek().similarity){
                    queue.poll();
                    queue.offer(new Similarity(user, similarity));
                }
            }
        }
//        for (UserMark userMark : queue){
//            System.out.println(userMark.name + " " + userMark.similarity);
//        }
        //5.开始高相似度用户借过的书，并将当前用户借过的过滤
        String sql3 = "SELECT book.bid AS bid,bname,bauthor,bpublisher,btype,bnum FROM borrow , book WHERE book.bid = borrow.bid AND " +
                "sid IN (?,?,?) AND brate>=5 AND borrow.bid NOT IN (\n" +
                "SELECT bid FROM borrow WHERE sid= ?) ";
        List<BookInfo> recommendList = jdbcTemplate.query(sql3, new BookInfoMapper(), queue.poll().name,queue.poll().name, queue.poll().name, sid);
        //System.out.println(recommendList);
        return recommendList;
    }

    public List<Double> getBookMark(String sid) {
        //获取当前用户对于所有书籍的打分，不存在评分为-1，置0
        List<Double> marks = new ArrayList<>();
        //将book和borrow通过左连接连接起来，即可获得所有书籍的评分，对于不存在借阅记录的书籍，将结果设置为0
        //得到的结果按bid升序排列，即为该用户的所有评分，同books顺序一致
        String sql = "SELECT IFNULL(brate,0) AS brate FROM book LEFT JOIN borrow\n" +
                "ON book.bid = borrow.bid\n" +
                "AND borrow.sid = ? \n" +
                "ORDER BY book.bid ASC";
        marks = jdbcTemplate.query(sql, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getDouble("brate");
            }
        }, sid);
        //System.out.println(marks);
        return marks;
    }

    /**
     * 基于物品的协同过滤推荐算法
     * 根据传入的bid，找出与他相似的书籍，返回书籍信息
     * @param bid
     * @return
     */
    public List<BookInfo> RecommendBasedItem(String bid){
        //1.获得所有有用户的名称，并升序存入列表
        String sql1 = "SELECT sid FROM `student`\n" +
                "ORDER BY sid ASC";
        List<String> users = jdbcTemplate.query(sql1, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("sid");
            }
        });
        System.out.println(users);
        //2.获得borrow中除当前book以外的其他书的bid，按升序排列，未在borrow中出现的书没被借过，故不推荐
        String sql2 = "SELECT DISTINCT bid FROM `borrow`\n" +
                "WHERE bid != ?\n" +
                "ORDER BY bid ASC";
        List<String> bookList = jdbcTemplate.query(sql2, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("bid");
            }
        }, bid);
        System.out.println(bookList);
        //3.获得当前书的用户评分列表
        List<Double> curMark = getUserMark(bid);
        PriorityQueue<Similarity> queue = new PriorityQueue<>(new Comparator<Similarity>() {
            @Override
            public int compare(Similarity o1, Similarity o2) {
                return (o1.similarity - o2.similarity)<0?-1:1;
            }
        });
        for (String book : bookList){
            //对于每一本书，获得其用户评分列表，分别于当前书籍计算相似度
            List<Double> mark = getUserMark(book);      //获得用户评分表
            //4.分别计算当前用户和其他用户的相似度
            //利用小根堆来得到相似度最高的前3个用户
            Double similarity = Pearson.getPearsonBydim(curMark, mark);
            //如果小根堆未满
            System.out.println(similarity);
            if (queue.size() < k)
                queue.offer(new Similarity(book, similarity));
            else {
                //小根堆满了
                if(similarity > queue.peek().similarity){
                    queue.poll();
                    queue.offer(new Similarity(book, similarity));
                }
            }
        }
        for (Similarity userMark : queue){
            System.out.println(userMark.name + " " + userMark.similarity);
        }
        //5.获得高相似度的书
        String sql3 = "SELECT book.bid AS bid,bname,bauthor,bpublisher,btype,bnum FROM book WHERE " +
                "bid IN (?,?,?) ";
        List<BookInfo> recommendList = jdbcTemplate.query(sql3, new BookInfoMapper(), queue.poll().name,queue.poll().name, queue.poll().name);
        //System.out.println(recommendList);
        return recommendList;
    }

    /**
     * 根据bid获得该书的用户评分列表，任何书籍获得的评分均按用户升序排列，故顺序一致
     * @param bid
     * @return
     */
    public List<Double> getUserMark(String bid) {
        //获取当前书籍 所有用户对其的打分，不存在评分置0
        List<Double> marks = new ArrayList<>();
        //将book和borrow通过左连接连接起来，即可获得所有书籍的评分，对于不存在借阅记录的书籍，将结果设置为0
        //得到的结果按bid升序排列，即为该用户的所有评分，同books顺序一致
        String sql = "SELECT\n" +
                "\tIFNULL( brate, 0 ) AS brate \n" +
                "FROM\n" +
                "\tstudent\n" +
                "\tLEFT JOIN borrow ON student.sid = borrow.sid \n" +
                "\tAND borrow.bid = ? \n" +
                "ORDER BY\n" +
                "\tstudent.sid ASC";
        marks = jdbcTemplate.query(sql, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getDouble("brate");
            }
        }, bid);
        System.out.println(marks);
        return marks;
    }
}
