package modernfarmer.server.farmuscommunity.community.service;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.ReportPostingRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.dto.response.WholePostingDto;
import modernfarmer.server.farmuscommunity.community.dto.response.WholePostingResponseDto;
import modernfarmer.server.farmuscommunity.community.entity.*;
import modernfarmer.server.farmuscommunity.community.repository.*;
import modernfarmer.server.farmuscommunity.community.util.TimeCalculator;
import modernfarmer.server.farmuscommunity.global.config.mail.MailSenderRunner;
import modernfarmer.server.farmuscommunity.global.config.s3.S3Uploader;
import modernfarmer.server.farmuscommunity.global.exception.fail.ErrorMessage;
import modernfarmer.server.farmuscommunity.global.exception.success.SuccessMessage;
import modernfarmer.server.farmuscommunity.user.UserServiceFeignClient;
import modernfarmer.server.farmuscommunity.user.dto.AllUserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class PostingService {



    private final UserServiceFeignClient userServiceFeignClient;
    private final S3Uploader s3Uploader;
    private final PostingRepository postingRepository;
    private final PostingImageRepository postingImageRepository;
    private final PostingReportRepository postingReportRepository;
    private final MailSenderRunner mailSenderRunner;
    private final TimeCalculator timeCalculator;


    public BaseResponseDto<Void> writePosting(Long userId,
                                        List<MultipartFile> multipartFiles,
                                        String title,
                                        String contents,
                                        String tag) {

        boolean ojectUrlResult = true;

        Posting posting = Posting
                .builder()
                .title(title)
                .contents(contents)
                .userId(userId)
                .tag(tag)
                .build();

        postingRepository.save(posting);

        if(!multipartFiles.get(0).isEmpty()){

            ojectUrlResult = s3Uploader.objectImageUrl(posting, multipartFiles);

        }

        if(!ojectUrlResult){

            return BaseResponseDto.of(ErrorMessage.OBJECT_URL_INSERT_ERROR);

        }

        log.info("게시글 작성 완료");

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);
    }

    public BaseResponseDto<Void> updatePosting(
            Long userId,
            List<String> removeFiles,
            List<MultipartFile> updateFiles,
            String title,
            String contents,
            Long postingId,
            String tag) {

        boolean ojectUrlResult = true;

        Posting posting = Posting
                .builder()
                .id(postingId)
                .title(title)
                .contents(contents)
                .userId(userId)
                .tag(tag)
                .build();

        postingRepository.updatePosting(userId, title, contents, postingId, tag);

        for(String image : removeFiles){

            postingImageRepository.deleteImage(image, posting);

        }

        if(!updateFiles.get(0).isEmpty()){

            ojectUrlResult = s3Uploader.objectImageUrl(posting, updateFiles);
        }

        if(!ojectUrlResult){

            return BaseResponseDto.of(ErrorMessage.OBJECT_URL_INSERT_ERROR);

        }

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);

    }

    public BaseResponseDto<Void> reportPosting(Long userId, ReportPostingRequest reportPostingRequest) throws Exception {


        // 테이블 데이터 넣기
        Posting posting = Posting.builder().id(reportPostingRequest.getPostingId()).build();


        PostingReport postingReport = PostingReport
                .builder()
                .posting(posting)
                .reportReason(reportPostingRequest.getReportReason())
                .userId(userId)
                .build();

        postingReportRepository.save(postingReport);

        // 메일 보내기
        mailSenderRunner.sendEmail("신고 메일입니다. ", reportPostingRequest.getReportReason() +" 이유로 신고가 왔습니다.");

        log.info("신고 완료");

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);

    }



    public BaseResponseDto<WholePostingResponseDto> getWholePosting(){

        List<Posting> list = postingRepository.allPostingSelect();

        modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto userData = userServiceFeignClient.allUser();
        Map<String, Object> userDataMap = (Map<String, Object>) userData.getData();
        List<Map<String, Object>> allUserDtoList = (List<Map<String, Object>>) userDataMap.get("allUserDtoList");


        Map<Integer, Map<String, Object>> userDtoMap = new HashMap<>();
        for (Map<String, Object> userDto : allUserDtoList) {
            Integer userId = (Integer) userDto.get("id");
            userDtoMap.put(userId, userDto);
        }

        List<WholePostingDto> wholePostingList = list.stream()
                .map(posting -> {
                    List<String> imageUrls = posting.getPostingImages().stream()
                            .map(PostingImage::getImageUrl)
                            .collect(Collectors.toList());

                    // 시간 형식 업데이트 로직
                    String formattedDate = timeCalculator.formatCreatedAt(posting.getCreatedAt());

                    Integer userId = Math.toIntExact(posting.getUserId());
                    Map<String, Object> userDto = userDtoMap.get(userId);

                    WholePostingDto.WholePostingDtoBuilder builder = WholePostingDto.builder()
                            .userId(userId)
                            .title(posting.getTitle())
                            .contents(posting.getContents())
                            .postingId(posting.getId())
                            .created_at(formattedDate)
                            .postingImage(imageUrls)
                            .tag(posting.getTag())
                            .commentCount(posting.getComments().size());


                    if (userDto != null) {
                        builder.nickName((String) userDto.get("nickName"))
                                .userImageUrl((String) userDto.get("imageUrl"));
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());

    log.info("전체 게시글 조회 완료");

    return BaseResponseDto.of(SuccessMessage.SUCCESS, WholePostingResponseDto.of(wholePostingList));
}

    public BaseResponseDto<WholePostingResponseDto> getMyPosting(Long userId){

        List<Posting> list = postingRepository.findByUserIdOrderByCreatedAtDesc(userId);

        List<WholePostingDto> specificUserWholePostingList = list.stream()
                .map(posting -> {
                    List<String> imageUrls = posting.getPostingImages().stream()
                            .map(PostingImage::getImageUrl)
                            .collect(Collectors.toList());


                    // 시간 형식 업데이트 로직
                    String formattedDate = timeCalculator.formatCreatedAt(posting.getCreatedAt());

                    WholePostingDto.WholePostingDtoBuilder builder = WholePostingDto.builder()
                            .userId(Math.toIntExact(userId))
                            .title(posting.getTitle())
                            .contents(posting.getContents())
                            .postingId(posting.getId())
                            .created_at(formattedDate)
                            .postingImage(imageUrls)
                            .tag(posting.getTag())
                            .commentCount(posting.getComments().size());

                    return builder.build();
                })
                .collect(Collectors.toList());
        log.info("나의 게시글 조회 완료");

        return BaseResponseDto.of(SuccessMessage.SUCCESS, WholePostingResponseDto.of(specificUserWholePostingList));
    }


    public void deleteAllPosting(Long userId){

        postingRepository.deleteAllPosting(userId);

        log.info("해당 계정의 모든 게시글 삭제");

      //  return BaseResponseDto.of(SuccessMessage.SUCCESS, null);
    }

}
