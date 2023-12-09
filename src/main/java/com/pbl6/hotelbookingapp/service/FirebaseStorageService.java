package com.pbl6.hotelbookingapp.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    public List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagePaths = new ArrayList<>();

        // Khởi tạo Storage từ tệp tin JSON của Firebase Service Account
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)))
                .build();
        Storage storage = storageOptions.getService();

        for (MultipartFile image : images) {
            try {
                String imageName = generateUniqueImageName(image);

                String storagePath = "images/" + imageName;

                // Upload image to Firebase Storage
                BlobId blobId = BlobId.of(bucketName, storagePath);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(image.getContentType()).build();
                byte[] bytes = image.getBytes();
                storage.create(blobInfo, bytes);

                String imageUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, storagePath);

                imagePaths.add(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload image to Firebase Storage");
            }
        }

        return imagePaths;
    }

    private String generateUniqueImageName(MultipartFile image) {
        return UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
    }
}
