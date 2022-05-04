package com.smartcampus.dao.Activity;

import com.smartcampus.utils.Pearson;
import com.smartcampus.utils.Similarity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 由于活动具有时效性，其他用户参与过的活动或者与当前活动相似度高的活动，不一定会再出现
 * 因此无法直接利用协同过滤得到推荐结果
 * 可以通过协同过滤算法
 * 基于用户，得到与当前用户相似度高的用户，并得到相关的活动，不直接将活动推荐给用户，而是根据活动的类型，将相同类型的活动推荐
 * 基于物品，得到与当前活动相似的活动，并根据活动类型，推荐相同类型的活动给用户
 */
public class ActivityRecommendDao {
    private JdbcTemplate jdbcTemplate;
    private int k = 3;

    public ActivityRecommendDao() {
    }

    public ActivityRecommendDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 基于用户的协同过滤算法
     *
     * @param sid
     * @return
     */
    public List<ActivityInfo> recommendbasedUser(String sid) {
        //1.获得除sid外所有参与过活动的用户，未参与过活动的用户不做推荐
        String sql1 = "SELECT sid FROM `engagement`\n" +
                "WHERE sid != ?";
        List<String> users = jdbcTemplate.query(sql1, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("sid");
            }
        }, sid);
        //2.开始获得所有用户的评分列表，按活动id - aid 升序排序，存入list，这样所有用户评分序列顺序一致
        List<Double> curMark = getActivitykMark(sid);        //当前用户的评分列表
        //开始获得其他用户的评分列表，并计算相似度

        PriorityQueue<Similarity> queue = new PriorityQueue<>(new Comparator<Similarity>() {
            @Override
            public int compare(Similarity o1, Similarity o2) {
                return (o1.similarity - o2.similarity) < 0 ? -1 : 1;
            }
        });

        for (String user : users) {
            List<Double> mark = getActivitykMark(user);      //获得用户评分表
            //4.分别计算当前用户和其他用户的相似度
            //利用小根堆来得到相似度最高的前3个用户
            Double similarity = Pearson.getPearsonBydim(curMark, mark);
            //如果小根堆未满
            //System.out.println(similarity);
            if (queue.size() < k)
                queue.offer(new Similarity(user, similarity));
            else {
                //小根堆满了
                if (similarity > queue.peek().similarity) {
                    queue.poll();
                    queue.offer(new Similarity(user, similarity));
                }
            }
        }
        //5.获得高相似度的用户参与过的活动类型
        String sql2 = "SELECT * FROM activity\n" +
                "WHERE  atheme IN (\n" +
                "SELECT atheme FROM engagement, activity\n" +
                "WHERE engagement.aid = activity.aid AND engagement.erate >= 5 AND engagement.sid IN (?,?,?)\n" +
                ")\n" +
                "LIMIT 3";
        List<ActivityInfo> recommendList = jdbcTemplate.query(sql2, new ActivityInfoMapper(),queue.poll().name,queue.poll().name, queue.poll().name);
        return recommendList;
    }

    /**
     * 根据sid活动该用户的活动评分列表
     *
     * @param sid
     * @return
     */
    public List<Double> getActivitykMark(String sid) {
        //获取当前用户对于所有书籍的打分，不存在评分置0
        List<Double> marks = new ArrayList<>();
        //将book和borrow通过左连接连接起来，即可获得所有书籍的评分，对于不存在借阅记录的书籍，将结果设置为0
        //得到的结果按bid升序排列，即为该用户的所有评分，同books顺序一致
        //如果用户对某一活动存在多次评分，取最大值
        String sql = "SELECT MAX(IFNULL(erate,0)) AS erate FROM activity LEFT JOIN engagement\n" +
                "ON activity.aid = engagement.aid\n" +
                "AND engagement.sid = ? \n" +
                "GROUP BY activity.aid\n "+
                "ORDER BY activity.aid ASC";
        marks = jdbcTemplate.query(sql, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getDouble("erate");
            }
        }, sid);
        //System.out.println(marks);
        return marks;
    }

}
