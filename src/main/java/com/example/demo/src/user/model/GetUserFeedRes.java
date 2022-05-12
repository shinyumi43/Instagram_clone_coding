package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserFeedRes {
    //나의 피드를 볼때와 다른 사람의 피드를 볼때 달라지는 화면 구성을 구별
    private boolean _isMyFeed;
    private GetUserInfoRes getUserInfo;
    private List<GetUserPostsRes> getUserPosts;
    //private int userIdx;
    //private String name;
    //private String nickName;
    //private String email;

}
