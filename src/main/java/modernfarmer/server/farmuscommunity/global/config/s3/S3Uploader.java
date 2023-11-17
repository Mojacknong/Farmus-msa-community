package modernfarmer.server.farmuscommunity.global.config.s3;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import modernfarmer.server.farmuscommunity.community.entity.Posting;
import modernfarmer.server.farmuscommunity.community.entity.PostingImage;
import modernfarmer.server.farmuscommunity.community.repository.PostingImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@RequiredArgsConstructor
@Slf4j
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    private final PostingImageRepository postingImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;



    public String uploadFile(
            MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(() ->
                new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile, dirName);
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> uploadUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            File uploadFile = convert(file).orElseThrow(() ->
                    new IllegalArgumentException("error: MultipartFile -> File convert fail"));
            String uploadUrl = upload(uploadFile, dirName);
            uploadUrls.add(uploadUrl);
        }
        return uploadUrls;
    }


    public String upload(File uploadFile, String filePath) {
        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            System.out.println("File delete success");
            return;
        }
        System.out.println("File delete fail");
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                fileOutputStream.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


    public boolean objectImageUrl(Posting posting, List<MultipartFile> multipartFiles ){

        try{

            List<String> imageUrls = uploadFiles(multipartFiles, "postingImages");

            for(String url : imageUrls){
                PostingImage postingImage = PostingImage
                        .builder()
                        .posting(posting)
                        .imageUrl(url)
                        .build();
                postingImageRepository.save(postingImage);
            }
        }catch (IOException e){
            return false;
        }
        return true;
    }
}