package modernfarmer.server.farmuscommunity.community.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.WritePostingRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.entity.*;
import modernfarmer.server.farmuscommunity.community.repository.PostingImageRepository;
import modernfarmer.server.farmuscommunity.community.repository.PostingRepository;
import modernfarmer.server.farmuscommunity.community.repository.PostingTagRepository;
import modernfarmer.server.farmuscommunity.community.repository.TagRepository;
import modernfarmer.server.farmuscommunity.global.config.s3.S3Uploader;
import modernfarmer.server.farmususer.global.exception.success.SuccessMessage;
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
    private  final PostingImageRepository postingImageRepository;
    private final TagRepository tagRepository;

    private final PostingTagRepository postingTagRepository;

    public BaseResponseDto writePosting(Long userId, List<MultipartFile> multipartFiles,
                                        String title,
                                        String contents,
                                        List<String> tags

    ) throws IOException {

        Posting posting = Posting
                .builder()
                .title(title)
                .contents(contents)
                .userId(userId)
                .build();

        postingRepository.save(posting);

        for(int i = 0; i < tags.size(); i++){

            Tag tag = tagRepository.findTagByAndTagName(tags.get(i));

            log.info(String.valueOf(tag.getTagName()));

            PostingTag postingTag = PostingTag
                    .builder()
                    .tag(tag)
                    .posting(posting)
                    .build();

            postingTagRepository.save(postingTag);

        }
        List<String> imageUrls = s3Uploader.uploadFiles(multipartFiles, "postingImages");

        for(String url : imageUrls){
            PostingImage postingImage = PostingImage
                    .builder()
                    .posting(posting)
                    .imageUrl(url)
                    .build();

            postingImageRepository.save(postingImage);
        }



        return BaseResponseDto.of(SuccessMessage.SUCCESS, null);



    }


}