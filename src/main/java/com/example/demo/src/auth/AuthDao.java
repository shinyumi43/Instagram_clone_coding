package com.example.demo.src.auth;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.User;
import com.example.demo.src.post.model.GetPostImgRes;
import com.example.demo.src.post.model.GetPostsRes;
import com.example.demo.src.post.model.PostImgsUrlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

//DB에 직접적으로 연결
@Repository
public class AuthDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public com.example.demo.src.auth.model.User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, name, nickName, email, password from User where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum)->new User(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email"),
                        rs.getString("password")
                ),getPwdParams
        );
    }
}
