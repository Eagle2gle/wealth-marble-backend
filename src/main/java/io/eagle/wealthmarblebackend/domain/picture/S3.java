package io.eagle.wealthmarblebackend.domain.picture;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import io.eagle.wealthmarblebackend.domain.picture.entity.Picture;
import io.eagle.wealthmarblebackend.domain.picture.repository.PictureRepository;
import io.eagle.wealthmarblebackend.domain.vacation.entity.Vacation;
import io.eagle.wealthmarblebackend.exception.S3Exception;
import io.eagle.wealthmarblebackend.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@ComponentScan(basePackages = "io.eagle.wealthmarblebackend")
@RequiredArgsConstructor
public class S3 {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final PictureRepository pictureRepository;

    public void saveFiles(List<MultipartFile> images, String type) {
        List<Picture> imageUrls = this.getUrlsFromS3(images, type);
        pictureRepository.saveAll(imageUrls);
    }

    public List<Picture> getUrlsFromS3(List<MultipartFile> images, String type) {
        List<Picture> imageUrls = new ArrayList<Picture>();
        for (MultipartFile image:images) {
            String url = this.uploadFile(image);
            imageUrls.add( Picture.builder()
                            .url(url)
                            .type(type)
                            .build());
        }
        return imageUrls;
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String contentType = getContentType(fileName);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(multipartFile.getSize());
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            throw new S3Exception(ErrorCode.S3_UPLOAD_ERROR);
        } catch (SdkClientException e) {
            throw new S3Exception(ErrorCode.S3_UPLOAD_ERROR);
        } catch (IOException e) {
            throw new S3Exception(ErrorCode.S3_UPLOAD_ERROR);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private String getContentType(String fileName){
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (ext) {
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpg";
            case "gif":
                return "image/gif";
        }
        throw new S3Exception(ErrorCode.S3_IMAGE_TYPE_INVALID);
    }

}
