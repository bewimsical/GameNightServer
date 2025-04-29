package edu.farmingdale.getgame.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final BlobContainerClient containerClient;

    @Autowired
    public ImageStorageService(BlobContainerClient containerClient) {  // Changed this line
        this.containerClient = containerClient;
    }

    public String uploadImage(String directory, MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String blobName = directory + "/" + fileName;
        
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());
        
        blobClient.upload(file.getInputStream(), file.getSize());
        blobClient.setHttpHeaders(headers);
        
        return blobClient.getBlobUrl();
    }

    public Optional<String> getImageUrl(String imagePath) {
        BlobClient blobClient = containerClient.getBlobClient(imagePath);
        return blobClient.exists() ? Optional.of(blobClient.getBlobUrl()) : Optional.empty();
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID().toString() + extension;
    }
}