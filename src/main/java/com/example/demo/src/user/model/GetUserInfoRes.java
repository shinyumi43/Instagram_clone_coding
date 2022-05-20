package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    //private int userIdx;
    //닉네임, 이름, 프로필 사진, 웹사이트, 소개글, 팔로워수, 팔로잉수, 게시물 개수
    private String nickName;
    private String name;
    private String profileImgUrl;
    private String website;
    private String introduce;
    private int followerCount;
    private int followingCount;
    private int postCount;
    //private GetUserInfoRes getUserInfo;
    //private List<getUserPostRes> getUserPosts;
    //private int userIdx;
    //private String name;
    //private String nickName;
    //private String email;

}
