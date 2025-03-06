package com.org.qualitycore.common;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.org.qualitycore.config.CloudinaryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinary = new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudinaryConfig.getCloudName(),
                        "api_key", cloudinaryConfig.getApiKey(),
                        "api_secret", cloudinaryConfig.getApiSecret()
                )
        );
    }

    // 이미지 업로드
    public String uploadImage(MultipartFile file) throws IOException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            if (imageUrl == null) {
                throw new IOException("Image URL is null after upload");
            }

            return imageUrl;  // 업로드된 이미지 URL 반환
        } catch (IOException e) {
            // 예외 처리: 클라우드너리에서 이미지를 업로드하는 중에 오류가 발생한 경우
            throw new IOException("Cloudinary image upload failed: " + e.getMessage(), e);
        }
    }

}
