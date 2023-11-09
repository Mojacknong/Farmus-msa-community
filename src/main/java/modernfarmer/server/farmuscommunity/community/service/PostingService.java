package modernfarmer.server.farmuscommunity.community.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.ReportPostingRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.dto.response.WholePostingDTO;

import modernfarmer.server.farmuscommunity.community.dto.response.WholePostingResponseDto;
import modernfarmer.server.farmuscommunity.community.entity.*;
import modernfarmer.server.farmuscommunity.community.repository.*;
import modernfarmer.server.farmuscommunity.global.config.mail.MailSenderRunner;
import modernfarmer.server.farmuscommunity.global.config.s3.S3Uploader;
import modernfarmer.server.farmuscommunity.global.exception.fail.ErrorMessage;
import modernfarmer.server.farmuscommunity.global.exception.success.SuccessMessage;
import modernfarmer.server.farmuscommunity.user.UserServiceFeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private final TagRepository tagRepository;
    private final PostingTagRepository postingTagRepository;
    private final PostingReportRepository postingReportRepository;
    private final MailSenderRunner mailSenderRunner;


    public BaseResponseDto writePosting(Long userId,
                                        List<MultipartFile> multipartFiles,
                                        String title,
                                        String contents,
                                        List<String> tags) {

        boolean ojectUrlResult = true;

        Posting posting = Posting
                .builder()
                .title(title)
                .contents(contents)
                .userId(userId)
                .build();

        postingRepository.save(posting);

        tagUpdate(posting, tags);

        if(!multipartFiles.get(0).isEmpty()){

            ojectUrlResult = objectImageUrl(posting, multipartFiles);

        }

        if(!ojectUrlResult){

            return BaseResponseDto.of(ErrorMessage.OBJECT_URL_INSERT_ERROR);

        }

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);
    }


    public BaseResponseDto updatePosting(
            Long userId,
            List<String> removeFiles,
            List<MultipartFile> updateFiles,
            String title,
            String contents,
            Long postingId,
            List<String> tags) {

        boolean ojectUrlResult = true;

        Posting posting = Posting
                .builder()
                .id(postingId)
                .title(title)
                .contents(contents)
                .userId(userId)
                .build();

        postingRepository.updatePosting(userId, title, contents, postingId);

        for(String image : removeFiles){
            postingImageRepository.deleteImage(image, posting);
            log.info(image);
        }

        postingTagRepository.deleteTag(posting);

        tagUpdate(posting, tags);

        if(!updateFiles.get(0).isEmpty()){

            ojectUrlResult = objectImageUrl(posting, updateFiles);
        }

        if(!ojectUrlResult){

            return BaseResponseDto.of(ErrorMessage.OBJECT_URL_INSERT_ERROR);

        }

        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);

    }

    public BaseResponseDto reportPosting(Long userId, ReportPostingRequest reportPostingRequest) throws Exception {


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

        // 완료
        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);

    }



    public BaseResponseDto getWholePosting(){

        List<Posting> list = postingRepository.allPostingSelect();

        modernfarmer.server.farmuscommunity.user.dto.BaseResponseDto userData = userServiceFeignClient.allUser();
        Map<String, Object> userDataMap = (Map<String, Object>) userData.getData();
        List<Map<String, Object>> allUserDtoList = (List<Map<String, Object>>) userDataMap.get("allUserDtoList");


        Map<Integer, Map<String, Object>> userDtoMap = new HashMap<>();
        for (Map<String, Object> userDto : allUserDtoList) {
            Integer userId = (Integer) userDto.get("id");
            userDtoMap.put(userId, userDto);
        }

        List<WholePostingDTO> wholePostingList = list.stream()
                .map(posting -> {
                    List<String> imageUrls = posting.getPostingImages().stream()
                            .map(PostingImage::getImageUrl)
                            .collect(Collectors.toList());

                    List<String> tagNames = posting.getPostingTags().stream()
                            .map(postingTag -> postingTag.getTag().getTagName())
                            .collect(Collectors.toList());

                    // 시간 형식 업데이트 로직
                    String formattedDate = formatCreatedAt(posting.getCreatedAt());

                    Integer userId = Math.toIntExact(posting.getUserId());
                    Map<String, Object> userDto = userDtoMap.get(userId);

                    WholePostingDTO.WholePostingDTOBuilder builder = WholePostingDTO.builder()
                            .userId(userId)
                            .title(posting.getTitle())
                            .contents(posting.getContents())
                            .postingId(posting.getId())
                            .created_at(formattedDate)
                            .postingImage(imageUrls)
                            .tagName(tagNames)
                            .commentCount(posting.getComments().size());

                    if (userDto != null) {
                        builder.nickName((String) userDto.get("nickName"))
                                .imageUrl((String) userDto.get("imageUrl"));
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());

    return BaseResponseDto.of(SuccessMessage.SUCCESS, WholePostingResponseDto.of(wholePostingList));

}

    private void tagUpdate(Posting posting, List<String> tags){


        for(int i = 0; i < tags.size(); i++){

            Tag tag = tagRepository.findTagByAndTagName(tags.get(i));

            PostingTag postingTag = PostingTag
                    .builder()
                    .tag(tag)
                    .posting(posting)
                    .build();

            postingTagRepository.save(postingTag);
        }
    }

    private boolean objectImageUrl(Posting posting,List<MultipartFile> multipartFiles ){

        try{

            List<String> imageUrls = s3Uploader.uploadFiles(multipartFiles, "postingImages");

            for(String url : imageUrls){
                PostingImage postingImage = PostingImage
                        .builder()
                        .posting(posting)
                        .imageUrl(url)
                        .build();
                postingImageRepository.save(postingImage);
            }
        }catch ( IOException e){
            return false;
        }
        return true;
    }

    private String formatCreatedAt(LocalDateTime createdAt) {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");

        return createdAt.atZone(zoneId).format(formatter);
    }

}
