package modernfarmer.server.farmuscommunity.community.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import modernfarmer.server.farmuscommunity.community.dto.request.DeleteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.UpdateCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.WriteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.service.CommentService;
import modernfarmer.server.farmuscommunity.community.service.PostingService;
import modernfarmer.server.farmuscommunity.community.util.JwtTokenProvider;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community/comment")
public class CommentController {
    private final JwtTokenProvider jwtTokenProvider;
    private  final CommentService commentService;


    @PostMapping("/write")
    public BaseResponseDto writeComment(HttpServletRequest request, @Validated @RequestBody WriteCommentRequest writeCommentRequestst){

        String userId = jwtTokenProvider.getUserId(request);

        BaseResponseDto baseResponseDto = commentService.writeComment(Long.valueOf(userId), writeCommentRequestst);

        return baseResponseDto;
    }

    @PatchMapping ("/update")
    public BaseResponseDto updateComment(HttpServletRequest request, @Validated @RequestBody UpdateCommentRequest updateCommentRequest){

        String userId = jwtTokenProvider.getUserId(request);

        BaseResponseDto baseResponseDto = commentService.updateComment(Long.valueOf(userId), updateCommentRequest);

        return baseResponseDto;
    }

    @DeleteMapping ("/delete")
    public BaseResponseDto deleteComment(HttpServletRequest request, @Validated @RequestBody DeleteCommentRequest deleteCommentRequest){

        String userId = jwtTokenProvider.getUserId(request);

        BaseResponseDto baseResponseDto = commentService.deleteComment(Long.valueOf(userId), deleteCommentRequest);

        return baseResponseDto;
    }

    @GetMapping("/posting-comments")
    public BaseResponseDto postingComment(@RequestParam("posterId") Long posterId){

        return commentService.postingComment(posterId);
    }





}
