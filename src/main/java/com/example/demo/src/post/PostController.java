package com.example.demo.src.post;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.post.model.GetPostsRes;
import com.example.demo.src.post.model.PatchPostsReq;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;



    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService){
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
    }



    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetPostsRes>> getPosts(@RequestParam int userIdx) {
        try{
            //현재 로그인이 된 계정과 조회를 할 피드의 계정에 대한 비교
            List<GetPostsRes> getPostsRes = postProvider.retrievePosts(userIdx);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //post method이기 때문에 body가 필요
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostsRes> createPosts(@RequestBody PostPostsReq postPostsReq) {
        try{
            if(postPostsReq.getContent().length() > 450){
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }
            if(postPostsReq.getPostImgUrls().size() < 1){
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_EMPTY_IMGURL);
            }
            PostPostsRes postPostsRes = postService.createPosts(postPostsReq.getUserIdx(), postPostsReq);
            return new BaseResponse<>(postPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{postIdx}")
    public BaseResponse<String> modifyPost(@PathVariable("postIdx") int postIdx, @RequestBody PatchPostsReq patchPostsReq) {
        try{
            //vaildation 처리
            if(patchPostsReq.getContent().length() > 450){
                return new BaseResponse<>(BaseResponseStatus.POST_POSTS_INVALID_CONTENTS);
            }
            //jwt를 구현할 때 더욱 쉽게 하도록 userIdx를 따로 빼준 것
            postService.modifyPost(patchPostsReq.getUserIdx(), postIdx, patchPostsReq);
            String result = "게시물 정보 수정을 완료하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{postIdx}/status")
    public BaseResponse<String> deletePost(@PathVariable("postIdx") int postIdx) {
        try{
            postService.deletePost(postIdx);
            String result = "삭제를 성공했습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
