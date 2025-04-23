package edu.farmingdale.getgame;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;

public class BlobConnection {
    private String storageAccountName;
    private String storageAccountKey;
    private String containerName;
    private final RestTemplate restTemplate;
    private static final String BLOB_ENDPOINT_TEMPLATE = "https://%s.blob.core.windows.net";

    public BlobConnection() {
        this.restTemplate = new RestTemplate();
        loadConfiguration();
        if (storageAccountName != null && storageAccountKey != null && containerName != null) {
            createContainerIfNotExists();
        } else {
            System.err.println("Azure Storage configuration not properly loaded. Check application.properties or environment variables.");
        }
    }

    private void loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("application.properties not found. Trying environment variables...");
                storageAccountName = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
                storageAccountKey = System.getenv("AZURE_STORAGE_ACCOUNT_KEY");
                containerName = System.getenv("AZURE_STORAGE_CONTAINER_NAME");
                if (storageAccountName == null || storageAccountKey == null || containerName == null) {
                    System.err.println("Azure Storage configuration not found in environment variables either.");
                }
                return;
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

    private void createContainerIfNotExists() {
        String url = String.format("%s/%s?restype=container", 
            String.format(BLOB_ENDPOINT_TEMPLATE, storageAccountName),
            containerName);

        HttpHeaders headers = createAuthHeaders("PUT", containerName, 0, "restype:container");
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Container '" + containerName + "' created.");
            }
        } catch (Exception e) {
            // 409 Conflict means container already exists, which is fine
            if (!e.getMessage().contains("409")) {
                System.err.println("Error creating container: " + e.getMessage());
            }
        }
    }

    private HttpHeaders createAuthHeaders(String httpMethod, String resource, long contentLength, String... additionalHeaders) {
        String date = DateTimeFormatter.RFC_1123_DATE_TIME
            .withZone(ZoneOffset.UTC)
            .format(Instant.now());

        String stringToSign = String.format("%s\n\n\n%d\n\n\n\n\n\n\n\n\nx-ms-date:%s\nx-ms-version:2020-04-08\n/%s/%s",
            httpMethod,
            contentLength,
            date,
            storageAccountName,
            resource);

        String signature = generateSignature(stringToSign);
        String authHeader = String.format("SharedKey %s:%s", storageAccountName, signature);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("x-ms-date", date);
        headers.set("x-ms-version", "2020-04-08");
        
        return headers;
    }

    private String generateSignature(String stringToSign) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                Base64.getDecoder().decode(storageAccountKey),
                "HmacSHA256"
            );
            sha256HMAC.init(secretKey);
            return Base64.getEncoder().encodeToString(
                sha256HMAC.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    public String getBaseUrl() {
        return String.format(BLOB_ENDPOINT_TEMPLATE, storageAccountName);
    }

    public String getContainerName() {
        return containerName;
    }

    public HttpHeaders createUploadHeaders(String blobName, long contentLength) {
        return createAuthHeaders("PUT", containerName + "/" + blobName, contentLength);
    }

    public HttpHeaders createDownloadHeaders(String blobName) {
        return createAuthHeaders("GET", containerName + "/" + blobName, 0);
    }
}