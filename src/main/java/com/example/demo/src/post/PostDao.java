package com.example.demo.src.post;
import com.example.demo.src.post.model.GetPostImgRes;
import com.example.demo.src.post.model.GetPostsRes;
import com.example.demo.src.post.model.PatchPostsReq;
import com.example.demo.src.post.model.PostImgsUrlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

//DB에 직접적으로 연결
@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;
    private List<GetPostImgRes> getPostImgRes; //게시물 리스트를 반환
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<GetPostsRes> selectPosts(int userIdx){
        String selectPostsQuery = "select p.postIdx as postIdx,\n" +
                "    u.userIdx as userIdx,\n" +
                "    u.nickName as nickName,\n" +
                "    u.profileImgUrl as profileImgUrl,\n" +
                "    p.content as content,\n" +
                "    if(postLikeCount is null, 0, postLikeCount) as postLikeCount,\n" +
                "    if(commentCount is null, 0, commentCount) as commentCount,\n" +
                "    case when timestampdiff(second, p.updatedAt, current_timestamp) < 60\n" +
                "    then concat(timestampdiff(second, p.updatedAt, current_timestamp), '초 전')\n" +
                "    when timestampdiff(minute, p.updatedAt, current_timestamp) < 60\n" +
                "    then concat(timestampdiff(minute, p.updatedAt, current_timestamp), '분 전')\n" +
                "    when timestampdiff(hour, p.updatedAt, current_timestamp) < 24\n" +
                "    then concat(timestampdiff(hour, p.updatedAt, current_timestamp), '시간 전')\n" +
                "    when timestampdiff(day, p.updatedAt, current_timestamp) < 365\n" +
                "    else concat(timestampdiff(day, p.updatedAt, current_timestamp), '일 전')\n" +
                "    end as UpdatedAt,\n" +
                "    if(pl.status = 'ACTIVE', 'Y', 'N') as likeOrNot\n" +
                "    from Post as p\n" +
                "    join User as u on u.userIdx = p.userIdx\n" +
                "    left join (select postIdx, userIdx, count(postLikeIdx) as postLikeCount from PostLike\n" +
                "    left join (select postIdx, count(commentIdx) as commentCount from Comment where status = 'ACTIVE')\n" +
                "    left join Follow as f on f.followeeIdx = p.userIdx and f.status = 'ACTIVE'\n" +
                "    left join PostLike as pl on pl.userIdx = f.followerIdx and pl.postIdx = p.postIdx\n" +
                "    where f.followerIdx = ? and p.status = 'ACTIVE'\n" +
                "    group by p.postIdx;";
        int selectPostsParam = userIdx;
        return this.jdbcTemplate.query(selectPostsQuery,
                (rs,rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("profileImgUrl"),
                        rs.getString("content"),
                        rs.getInt("postLikeCount"),
                        rs.getInt("commentCount"),
                        rs.getString("UpdatedAt"),
                        rs.getString("likeOrNot"),
                        getPostImgRes=this.jdbcTemplate.query("select pi.postImgIdx, pi.imgUrl\n" +
                                "from PostImgUrl as pi\n" +
                                "join Post as p on p.postIdx = pi.postIdx\n" +
                                "where pi.status = 'ACTIVE' and p.postIdx = ?",
                                (rk,rownum)->new GetPostImgRes(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("imgUrl")
                                ),rs.getInt("postIdx")
                                )
                ), selectPostsParam);
    }

    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }

    public int checkPostExist(int postIdx){
        String checkPostExistQuery = "select exists(select postIdx from Post where postIdx = ?)";
        int checkPostExistParams = postIdx;
        return this.jdbcTemplate.queryForObject(checkPostExistQuery,
                int.class,
                checkPostExistParams);
    }
    
    //게시물을 생성하는 쿼리
    public int insertPosts(int userIdx, String content){
        String insertPostQuery = "insert into Post(userIdx, content) values (?, ?)";
        Object []insertPostParams = new Object[] {userIdx, content};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public int insertPostsImgs(int postIdx, PostImgsUrlReq postImgsUrlReq){
        String insertPostImgsQuery = "insert into PostImgUrl(postIdx, imgUrl) values (?, ?)";
        Object []insertPostImgsParams = new Object[] {postIdx, postImgsUrlReq.getImgUrl()};
        this.jdbcTemplate.update(insertPostImgsQuery,
                insertPostImgsParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public int updatePost(int postIdx, String content){
        String updatePostQuery = "update Post set content=? where postIdx=?";
        Object []updatePostParams = new Object[] {content, postIdx};
        this.jdbcTemplate.update(updatePostQuery, updatePostParams);
        String lastInsertIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public int deletePost(int postIdx){
        String deletePostQuery = "update Post set status='INACTIVE' where postIdx=?";
        Object []deletePostParams = new Object[] {postIdx};
        return this.jdbcTemplate.update(deletePostQuery, deletePostParams);
    }



}
