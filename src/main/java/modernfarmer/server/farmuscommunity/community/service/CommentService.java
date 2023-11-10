package modernfarmer.server.farmuscommunity.community.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.DeleteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.UpdateCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.WriteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.dto.response.PostingCommentDto;
import modernfarmer.server.farmuscommunity.community.dto.response.WholePostingDto;
import modernfarmer.server.farmuscommunity.community.entity.Comment;
import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.entity.PostingImage;
import modernfarmer.server.farmuscommunity.community.repository.*;
import modernfarmer.server.farmuscommunity.global.exception.fail.ErrorMessage;
import modernfarmer.server.farmuscommunity.global.exception.success.SuccessMessage;
import modernfarmer.server.farmuscommunity.user.UserServiceFeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class CommentService {


    private final CommentRepository commentRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final PostingService postingService;

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

    public BaseResponseDto postingComment(Long postingId){


        modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto userData = userServiceFeignClient.allUser();
        Map<String, Object> userDataMap = (Map<String, Object>) userData.getData();
        List<Map<String, Object>> allUserDtoList = (List<Map<String, Object>>) userDataMap.get("allUserDtoList");


        Map<Integer, Map<String, Object>> userDtoMap = new HashMap<>();
        for (Map<String, Object> userDto : allUserDtoList) {
            Integer userId = (Integer) userDto.get("id");
            userDtoMap.put(userId, userDto);
        }


        List<Comment> commentList = commentRepository.findByPostingId(postingId);

        if(commentList.isEmpty()){
            return BaseResponseDto.of(ErrorMessage.NOT_EXIST_COMMENT);
        }


        List<PostingCommentDto> postingCommentList = commentList.stream()
                .map(comment -> {

                    // 시간 형식 업데이트 로직
                    String formattedDate = postingService.formatCreatedAt(comment.getCreatedAt());

                    Integer userId = Math.toIntExact(comment.getUserId());
                    Map<String, Object> userDto = userDtoMap.get(userId);

                    PostingCommentDto.PostingCommentDtoBuilder builder = PostingCommentDto.builder()
                            .userId(userId)
                            .postingId(postingId)
                            .commentContents(comment.getCommentContents())
                            .commentCount(commentList.size())
                            .created_at(formattedDate);

                    if (userDto != null) {
                        builder.nickName((String) userDto.get("nickName"))
                                .userImageUrl((String) userDto.get("imageUrl"));
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());

        return BaseResponseDto.of(SuccessMessage.SUCCESS, postingCommentList);
    }


}
