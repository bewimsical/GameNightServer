package edu.farmingdale.getgame;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BlobConnection {

    @Autowired
    private BlobContainerClient blobContainerClient;

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique name for the blob
        String fileName = generateUniqueName(file.getOriginalFilename());
        
        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        // Set the content type
        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(file.getContentType());
        
        // Upload the file
        blobClient.upload(file.getInputStream(), file.getSize());
        blobClient.setHttpHeaders(headers);

        // Return the URL of the uploaded file
        return blobClient.getBlobUrl();
    }

    public void deleteFile(String fileName) {
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.deleteIfExists();
    }

    private String generateUniqueName(String originalFileName) {
        return System.currentTimeMillis() + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}