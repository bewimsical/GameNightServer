import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Blobconnection {

    private static final String storageAccountName = "<your_storage_account_name>";
    private static final String storageAccountKey = "<your_storage_account_key>";
    private static final String containerName = "image-container";

    public static void main(String[] args) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(String.format("https://%s.blob.core.windows.net", storageAccountName))
                .credential(new StorageSharedKeyCredential(storageAccountName, storageAccountKey))
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        // Upload a file
        String localFilePath = "/path/to/your/local/image.jpg";
        String blobName = "uploaded-image.jpg";
        uploadBlob(containerClient, localFilePath, blobName);

        // List blobs in the container
        listBlobs(containerClient);

        // Get blob URL
        String blobUrl = getBlobUrl(containerClient, blobName);
        System.out.println("Blob URL: " + blobUrl);

        // Download a blob
        String downloadFilePath = "/path/to/save/downloaded-image.jpg";
        downloadBlob(containerClient, blobName, downloadFilePath);
    }

    private static void uploadBlob(BlobContainerClient containerClient, String localFilePath, String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        File localFile = new File(localFilePath);
        try (FileInputStream data = new FileInputStream(localFile)) {
            blobClient.upload(data, localFile.length());
            System.out.println("Uploaded " + blobName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listBlobs(BlobContainerClient containerClient) {
        System.out.println("Listing blobs in container:");
        for (BlobItem blobItem : containerClient.listBlobs()) {
            System.out.println("- " + blobItem.getName());
        }
    }

    private static String getBlobUrl(BlobContainerClient containerClient, String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        return blobClient.getBlobUrl();
    }

    private static void downloadBlob(BlobContainerClient containerClient, String blobName, String downloadFilePath) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        try {
            blobClient.downloadToFile(downloadFilePath);
            System.out.println("Downloaded " + blobName + " to " + downloadFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}