package edu.farmingdale.getgame;

import com.azure.core.util.BinaryData;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;

public class UserImageTest {

    private static String storageAccountName;
    private static String storageContainerName;
    private static String mysqlConnectionString;
    private static String mysqlUser;
    private static String mysqlPassword;

    public static void main(String[] args) {
        loadConfiguration();

        // 1. Upload a picture to Azure Blob Storage
        String imageUrl = uploadImageToBlob("test-image.jpg", "This is a test image content"); // Replace with actual image data if needed

        if (imageUrl != null) {
            System.out.println("Image uploaded successfully. URL: " + imageUrl);

            // 2. Store the image URL in Azure MySQL
            if (storeImageUrlInMySQL(imageUrl, "user123")) { // Use a unique user identifier
                System.out.println("Image URL stored in MySQL.");

                // 3. Simulate another user retrieving the image URL and "displaying" the image.
                retrieveAndDisplayImage("user123");
            } else {
                System.err.println("Failed to store image URL in MySQL.");
            }
        } else {
            System.err.println("Failed to upload image to Blob Storage.");
        }
    }

    private static void loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream input = UserImageTest.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("application.properties file not found.  Exiting.");
                System.exit(1);
            }
            properties.load(input);

            storageAccountName = properties.getProperty("azure.storage.account.name");
            storageContainerName = properties.getProperty("azure.storage.container.name");
            mysqlConnectionString = properties.getProperty("azure.mysql.connection.string");
            mysqlUser = properties.getProperty("azure.mysql.username");
            mysqlPassword = properties.getProperty("azure.mysql.password");

            if (storageAccountName == null || storageContainerName == null || mysqlConnectionString == null || mysqlUser == null || mysqlPassword == null) {
                System.err.println("One or more configuration properties are missing.  Check application.properties. Exiting.");
                System.exit(1);
            }

        } catch (IOException ex) {
            System.err.println("Error loading application.properties: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static String uploadImageToBlob(String imageName, String imageContent) {
        try {
            // Build the Blob Service Client.
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .accountName(storageAccountName)
                    .credential(new DefaultAzureCredentialBuilder().build())
                    .buildClient();

            //Get the container
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(storageContainerName);
            containerClient.createIfNotExists();

            // Create a Blob Client
            BlobClient blobClient = containerClient.getBlobClient(imageName);

            // Upload the image data (using a string for simplicity in this example)
            try (InputStream dataStream = new ByteArrayInputStream(imageContent.getBytes(StandardCharsets.UTF_8))) {
                blobClient.upload(dataStream, imageContent.length(), true);
            }

            // Return the URL of the uploaded blob
            return blobClient.getBlobUrl();
        } catch (Exception e) {
            System.err.println("Error uploading image to Blob Storage: " + e.getMessage());
            e.printStackTrace();
            return null; // Handle the error appropriately
        }
    }

    private static boolean storeImageUrlInMySQL(String imageUrl, String userId) {
        try (Connection connection = DriverManager.getConnection(mysqlConnectionString, mysqlUser, mysqlPassword)) {
            // Use a parameterized query to prevent SQL injection
            String sql = "INSERT INTO user_images (user_id, image_url) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, imageUrl);
                preparedStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error storing image URL in MySQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void retrieveAndDisplayImage(String userId) {
        try (Connection connection = DriverManager.getConnection(mysqlConnectionString, mysqlUser, mysqlPassword)) {
            String sql = "SELECT image_url FROM user_images WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String imageUrl = resultSet.getString("image_url");
                    System.out.println("Retrieved image URL from MySQL: " + imageUrl);
                    // In a real application, you would use this URL to display the image in a browser or application.
                    System.out.println("Simulating image display from URL: " + imageUrl);
                } else {
                    System.out.println("No image URL found for user: " + userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving image URL from MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}