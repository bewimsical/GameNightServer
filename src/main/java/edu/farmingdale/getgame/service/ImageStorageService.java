package edu.farmingdale.getgame.service;

import com.azure.storage.blob.*;
import com.azure.core.util.BinaryData;
import edu.farmingdale.getgame.AzureStorageConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

@Service
public class ImageStorageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageStorageService.class);
    private final AzureStorageConfig config;
    private final BlobServiceClient blobServiceClient;

    public ImageStorageService(AzureStorageConfig config) {
        this.config = config;
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(config.getStorageConnectionString())
                .buildClient();
    }

    public String uploadImage(String userId, MultipartFile imageFile) throws IOException {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be null or empty");
        }

        try {
            // Generate unique image name
            String imageName = generateUniqueImageName(userId, imageFile.getOriginalFilename());
            
            // Get container client
            BlobContainerClient containerClient = blobServiceClient
                    .getBlobContainerClient(config.getContainerName());
            
            // Create container if it doesn't exist
            containerClient.createIfNotExists();

            // Upload file
            BlobClient blobClient = containerClient.getBlobClient(imageName);
            blobClient.upload(BinaryData.fromBytes(imageFile.getBytes()), true);

            // Get URL
            String imageUrl = blobClient.getBlobUrl();

            // Store URL in database
            storeImageUrlInDatabase(userId, imageUrl);

            logger.info("Successfully uploaded image for user: {}", userId);
            return imageUrl;
        } catch (Exception e) {
            logger.error("Failed to upload image for user: {}", userId, e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private String generateUniqueImageName(String userId, String originalFilename) {
        return userId + "_" + System.currentTimeMillis() + "_" + 
               originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private void storeImageUrlInDatabase(String userId, String imageUrl) {
        String sql = "INSERT INTO users (user_id, profile_pic_url) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(
                config.getMysqlConnectionString(),
                config.getMysqlUsername(),
                config.getMysqlPassword());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, imageUrl);
            pstmt.executeUpdate();
            pstmt.executeUpdate();
            logger.debug("Stored image URL in database for user: {}", userId);
        } catch (SQLException e) {
            logger.error("Failed to store image URL in database for user: {}", userId, e);
            throw new RuntimeException("Failed to store image URL in database", e);
        }
    }

    public Optional<String> getImageUrl(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String sql = "SELECT profile_pic_url FROM users WHERE user_id = ?";
        
        try (Connection conn = DriverManager.getConnection(
                config.getMysqlConnectionString(),
                config.getMysqlUsername(),
                config.getMysqlPassword());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String imageUrl = rs.getString(1);
                    logger.debug("Retrieved image URL for user: {}", userId);
                    return Optional.of(imageUrl);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve image URL for user: {}", userId, e);
            throw new RuntimeException("Failed to retrieve image URL from database", e);
        }
        return Optional.empty();
    }
}