package edu.farmingdale.getgame;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BlobConnection {

    private String storageAccountName;
    private String storageAccountKey;
    private String containerName;
    private BlobServiceClient blobServiceClient;

    public BlobConnection() {
        loadConfiguration(); // Load from properties
        if (storageAccountName != null && storageAccountKey != null && containerName != null) {
            createBlobServiceClient();
            createContainerIfNotExists();
        } else {
            System.err.println("Azure Storage configuration not properly loaded. Check application.properties or environment variables.");
            // Consider throwing an exception here to enforce proper configuration
        }
    }
    private void loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("application.properties not found. Trying environment variables...");
                // Load from environment variables as a fallback
                storageAccountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
                storageAccountKey = System.getenv("AZURE_STORAGE_ACCOUNT_KEY");
                containerName = System.getenv("AZURE_STORAGE_CONTAINER_NAME");
                if (storageAccountName == null || storageAccountKey == null || containerName == null) {
                    System.err.println("Azure Storage configuration not found in environment variables either.");

                }
                return; // Exit the method after trying env vars
            }
            properties.load(input);
            storageAccountName = properties.getProperty("azure.storage.account.name");
            storageAccountKey = properties.getProperty("azure.storage.account.key");
            containerName = properties.getProperty("azure.storage.container.name");
        } catch (IOException ex) {
            System.err.println("Error loading application.properties: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void createBlobServiceClient() {
        // Use the credentials
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(storageAccountName, storageAccountKey);
        this.blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", storageAccountName))
                .credential(credential)
                .buildClient();
    }

    private void createContainerIfNotExists() {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!containerClient.exists()) {
                containerClient.create();
                System.out.println("Container '" + containerName + "' created.");
            }
        } catch (Exception e) {
            System.err.println("Error creating container if not exists: " + e.getMessage());
        }

    }

    public BlobServiceClient getBlobContainerClient() {
        return blobServiceClient;
    }

    // Add other methods for interacting with Blob Storage as needed
}
