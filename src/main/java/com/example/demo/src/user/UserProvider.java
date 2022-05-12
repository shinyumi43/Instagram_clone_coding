package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.GetUserFeedRes;
import com.example.demo.src.user.model.GetUserInfoRes;
import com.example.demo.src.user.model.GetUserPostRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }


    public GetUserFeedRes retrieveUser(int userIdxByJwt, int userIdx) throws BaseException{
        // Jwt에서 받아온 userIdx는 현재 로그인된 정보, userIdx는 pathvariable로 받은 것
        Boolean isMyFeed = true;
        try{
            if(userIdxByJwt != userIdx)
                isMyFeed = false;
            GetUserInfoRes getUserInfoRes = userDao.selectUserInfo(userIdx);
            List<GetUserPostRes> getUserPosts = userDao.selectUserPosts(userIdx);
            GetUserFeedRes getUsersRes = GetUserFeedRes(isMyFeed.getUserInfo, getUserPosts);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
                    }

    //이메일 조회 당시 사용한 함수를 수정해서 사용, 이 다음에 Dao에 가서 생성
    public GetUserFeedRes getUsersByIdx(int userIdx) throws BaseException{
        try{
            GetUserFeedRes getUsersRes = userDao.getUsersByIdx(userIdx);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
