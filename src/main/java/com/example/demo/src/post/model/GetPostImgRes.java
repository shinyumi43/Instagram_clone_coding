package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostImgRes {
    //사진을 반환하는 객체
    private int postImgIdx;
    private String imgUrl;
}
