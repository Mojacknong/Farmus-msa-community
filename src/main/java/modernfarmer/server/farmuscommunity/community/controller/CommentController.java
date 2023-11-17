package modernfarmer.server.farmuscommunity.community.controller;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.DeleteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.UpdateCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.WriteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.dto.response.PostingCommentResponseDto;
import modernfarmer.server.farmuscommunity.community.service.CommentService;
import modernfarmer.server.farmuscommunity.community.util.JwtTokenProvider;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community/comment")
public class CommentController {

    private final JwtTokenProvider jwtTokenProvider;

    private  final CommentService commentService;

    @PostMapping("/write")
    public BaseResponseDto<Void> writeComment(HttpServletRequest request, @Validated @RequestBody WriteCommentRequest writeCommentRequestst){

        String userId = jwtTokenProvider.getUserId(request);

        return commentService.writeComment(Long.valueOf(userId), writeCommentRequestst);
    }

    @PatchMapping ("/update")
    public BaseResponseDto<Void> updateComment(HttpServletRequest request, @Validated @RequestBody UpdateCommentRequest updateCommentRequest){

        String userId = jwtTokenProvider.getUserId(request);


        return commentService.updateComment(Long.valueOf(userId), updateCommentRequest);
    }

    @DeleteMapping ("/delete")
    public BaseResponseDto<Void> deleteComment(HttpServletRequest request, @Validated @RequestBody DeleteCommentRequest deleteCommentRequest){

        String userId = jwtTokenProvider.getUserId(request);

        return commentService.deleteComment(Long.valueOf(userId), deleteCommentRequest);
    }

    @GetMapping("/posting-comments")
    public BaseResponseDto<PostingCommentResponseDto> postingComment(@RequestParam("posterId") Long posterId,
                                                                     @RequestParam("userId") Long userId
                                          ){

        return commentService.postingComment(posterId, userId);
    }

}
