package modernfarmer.server.farmuscommunity.community.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.dto.request.ReportPostingRequest;
import modernfarmer.server.farmuscommunity.community.dto.response.BaseResponseDto;
import modernfarmer.server.farmuscommunity.community.entity.ReportTag;
import modernfarmer.server.farmuscommunity.community.repository.ReportTagRepository;
import modernfarmer.server.farmuscommunity.community.service.PostingService;
import modernfarmer.server.farmuscommunity.community.util.JwtTokenProvider;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/community/posting")
public class PostingController {

    private final JwtTokenProvider jwtTokenProvider;
    private  final PostingService postingService;
    private final ReportTagRepository reportTagRepository;

    @PostMapping("/write")
    public BaseResponseDto writePosting(HttpServletRequest request, @RequestParam("file") List<MultipartFile> multipartFiles,
                                        @RequestParam("title") String title,
                                        @RequestParam("contents") String contents,
                                        @RequestParam("tags") List<String> tags
    ) throws IOException {


        String userId = jwtTokenProvider.getUserId(request);

        return postingService.writePosting(Long.valueOf(userId), multipartFiles, title, contents, tags);
    }

    @PostMapping("/report")
    public BaseResponseDto reportPosting(HttpServletRequest request, @Validated @RequestBody ReportPostingRequest reportPostingRequest) throws Exception {

        String userId = jwtTokenProvider.getUserId(request);

        return postingService.reportPosting(Long.valueOf(userId), reportPostingRequest);
    }

    @PatchMapping("/report-update")
    public BaseResponseDto updatePosting(HttpServletRequest request,
                                         @RequestParam("removeFile") List<String> removeFiles,
                                         @RequestParam("updateFile") List<MultipartFile> updateFiles,
                                         @RequestParam("title") String title,
                                         @RequestParam("contents") String contents,
                                         @RequestParam("postingId") Long postingId,
                                         @RequestParam("tags") List<String> tags) {

        String userId = jwtTokenProvider.getUserId(request);

        return postingService.updatePosting(Long.valueOf(userId), removeFiles, updateFiles, title, contents, postingId, tags);
    }


    @GetMapping("/report-tag")
    public BaseResponseDto getReportTag() {

        return postingService.getReportTag();
    }

}
