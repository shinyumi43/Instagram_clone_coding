package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostsReq {
    //게시물 수정, 어떤 유저가 수정을 할지, 내용은 어떻게 할지를 결정
    private int userIdx;
    private String content;
}
