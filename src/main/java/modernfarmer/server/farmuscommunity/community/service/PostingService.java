package modernfarmer.server.farmuscommunity.community.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.ReportPostingRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.entity.*;
import modernfarmer.server.farmuscommunity.community.repository.*;
import modernfarmer.server.farmuscommunity.global.config.mail.MailSenderRunner;
import modernfarmer.server.farmuscommunity.global.config.s3.S3Uploader;
import modernfarmer.server.farmuscommunity.global.exception.fail.ErrorMessage;
import modernfarmer.server.farmuscommunity.global.exception.success.SuccessMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class PostingService {



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

        // 게시글 id, 제목, 내용, 업로드 시간,이미지 배열, 유저 id 게시글에 달린 댓글 개수 가져오기 함수(시간 순서로)

        // 유저 id , 기반으로 유저 닉네임, 프로필 사진 url 가져오기 함수(페인)



        // 시간 형식 업데이트 로직





    return BaseResponseDto.of(SuccessMessage.SUCCESS, null);

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

}
