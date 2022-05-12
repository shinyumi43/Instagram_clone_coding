package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserFeedRes {
    //나의 피드를 볼때와 다른 사람의 피드를 볼때 다른 상황을 구별
    private boolean inMyFeed;
    private GetUserInfoRes getUserInfo;
    private List<GetUserPostRes> getUserPosts;
    //private int userIdx;
    //private String name;
    //private String nickName;
    //private String email;

}
