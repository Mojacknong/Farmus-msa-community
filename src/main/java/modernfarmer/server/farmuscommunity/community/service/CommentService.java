package modernfarmer.server.farmuscommunity.community.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.DeleteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.UpdateCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.request.WriteCommentRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.*;
import modernfarmer.server.farmuscommunity.community.entity.Comment;
import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.entity.PostingImage;
import modernfarmer.server.farmuscommunity.community.repository.CommentRepository;
import modernfarmer.server.farmuscommunity.community.repository.PostingRepository;
import modernfarmer.server.farmuscommunity.global.exception.fail.ErrorMessage;
import modernfarmer.server.farmuscommunity.global.exception.success.SuccessMessage;
import modernfarmer.server.farmuscommunity.user.UserServiceFeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class CommentService {


    private final CommentRepository commentRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final PostingService postingService;
    private final PostingRepository postingRepository;

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

    public BaseResponseDto postingComment(Long postingId, Long userId){


        modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto userData = userServiceFeignClient.allUser();
        modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto speicificUserData = userServiceFeignClient.specificUser(userId);

        Map<String, Object> userDataMap = (Map<String, Object>) userData.getData();
        LinkedHashMap<String, Object> speicificUserDataMap = (LinkedHashMap<String, Object>) speicificUserData.getData();

        List<Map<String, Object>> allUserDtoList = (List<Map<String, Object>>) userDataMap.get("allUserDtoList");


        Map<Integer, Map<String, Object>> userDtoMap = new HashMap<>();
        for (Map<String, Object> userDto : allUserDtoList) {
            Integer getUserId = (Integer) userDto.get("id");
            userDtoMap.put(getUserId, userDto);
        }

        List<Comment> commentList = commentRepository.findByPostingId(postingId);
        Optional<Posting> posting = postingRepository.findById(postingId);

        if(posting.isEmpty()){
            return BaseResponseDto.of(ErrorMessage.NOT_EXIST_POSTING);
        }

        List<String> list =  posting.get().getPostingImages().stream().map(PostingImage::getImageUrl)
                .collect(Collectors.toList());

        WholePostingDto wholePostingDto = WholePostingDto
                .builder()
                .userId(Math.toIntExact(userId))
                .nickName((String) speicificUserDataMap.get("nickName"))
                .userImageUrl((String) speicificUserDataMap.get("imageUrl"))
                .postingId(postingId)
                .tag(posting.get().getTag())
                .title(posting.get().getTitle())
                .contents(posting.get().getContents())
                .postingImage(list)
                .created_at(postingService.formatCreatedAt(posting.get().getCreatedAt()))
                .build();


        List<PostingCommentDto> postingCommentList = commentList.stream()
                .map(comment -> {

                    // 시간 형식 업데이트 로직
                    String formattedDate = postingService.formatCreatedAt(comment.getCreatedAt());

                    Integer getUserId = Math.toIntExact(comment.getUserId());
                    Map<String, Object> userDto = userDtoMap.get(getUserId);

                    PostingCommentDto.PostingCommentDtoBuilder builder = PostingCommentDto.builder()
                            .userId(getUserId)
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

        return BaseResponseDto.of(SuccessMessage.SUCCESS, PostingCommentResponseDto.of(wholePostingDto, postingCommentList));
    }


}
