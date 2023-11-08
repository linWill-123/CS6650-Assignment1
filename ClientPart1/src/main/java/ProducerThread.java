import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ProducerThread implements Callable<LogResult> {
    private ApiClient client;
    private DefaultApi apiInstance;
    private String imagePath;
    private int numIterations;
    private int numSuccess = 0;
    private int numFailure = 0;
    private final CountDownLatch latch;

    public ProducerThread(String serverUrl, String imagePath, int numIterations, CountDownLatch latch) {
        this.client = new ApiClient();
        this.client.setBasePath(serverUrl);
        this.apiInstance = new DefaultApi(client);
        this.imagePath = imagePath;
        this.numIterations = numIterations;
        this.latch = latch;
    }

    @Override
    public LogResult call() {
        File image = new File(imagePath);
        AlbumsProfile profile = new AlbumsProfile();
        profile.setArtist("Eminem");
        profile.setTitle("MMlp2");
        profile.setYear("2001");

        for (int i = 0; i < numIterations; i++) {
            doPost(image, profile);
            doGet("1");
        }

        latch.countDown();
        return new LogResult(numSuccess,numFailure);
    }

    public void doPost(File image, AlbumsProfile profile) {
        boolean success = false;
        int attempts = 0;

        while (!success && attempts < 5) {
            attempts++;

            try {
                ImageMetaData postResponse = apiInstance.newAlbum(image, profile);
                numSuccess++;
                success = true; // Mark as success and break the loop
            } catch (ApiException e) {
                if (attempts >= 5) {
                    System.err.println("Attempt " + attempts + " Error in POST: " + e.getMessage());
                    numFailure++; // Only increment failure after all retries have been attempted
                }
            }
        }
    }

    public void doGet(String albumId) {
        boolean success = false;
        int attempts = 0;

        while (!success && attempts < 5) {
            attempts++;
            try {
                AlbumInfo getResponse = apiInstance.getAlbumByKey(albumId);
                numSuccess++;
                success = true; // Mark as success and break the loop
            } catch (ApiException e) {
                if (attempts >= 5) {
                    System.err.println("Attempt " + attempts + " Error in GET: " + e.getMessage());
                    numFailure++; // Only increment failure after all retries have been attempted
                }
            }
        }
    }

}
