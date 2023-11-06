package modernfarmer.server.farmuscommunity.community.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.DeleteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.UpdateCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.WriteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.entity.Comment;
import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.repository.*;
import modernfarmer.server.farmususer.global.exception.success.SuccessMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class CommentService {

    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;

    public BaseResponseDto writeComment(Long userId, WriteCommentRequest writeCommentRequest){

        Posting posting = Posting.builder().id(writeCommentRequest.getPostingId()).build();

        Comment comment = Comment
                .builder()
                .commentContents(writeCommentRequest.getComment())
                .userId(userId)
                .posting(posting)
                .build();

        commentRepository.save(comment);

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);
    }

    public BaseResponseDto updateComment(Long userId, UpdateCommentRequest updateCommentRequest){

        Posting posting = Posting.builder().id(updateCommentRequest.getPostingId()).build();

        commentRepository.updateComment(userId, posting,updateCommentRequest.getCommentId(),updateCommentRequest.getComment());

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);
    }

    public BaseResponseDto deleteComment(Long userId, DeleteCommentRequest deleteCommentRequest){

        Posting posting = Posting.builder().id(deleteCommentRequest.getPostingId()).build();

        commentRepository.deleteComment(userId, posting,deleteCommentRequest.getCommentId());

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);
    }


}
