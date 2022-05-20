package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPostsReq {
    //게시물 전체를 반환하는 객체
    private int userIdx;
    private String content;
    private List<PostImgsUrlReq> postImgUrls;
}
