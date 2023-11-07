import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Callable;

public class ProducerThread implements Callable<LogResult> {
    private ApiClient client;
    private DefaultApi apiInstance;
    private String imagePath;
    private int numIterations;
    private int numSuccess = 0;
    private int numFailure = 0;
    private final List<String> logEntries;
    private final CountDownLatch latch;

    public ProducerThread(String serverUrl, String imagePath, int numIterations, CountDownLatch latch) {
        this.client = new ApiClient();
        this.client.setBasePath(serverUrl);
        this.apiInstance = new DefaultApi(client);
        this.imagePath = imagePath;
        this.numIterations = numIterations;
        this.logEntries = new ArrayList<>();
        this.latch = latch;
    }

    @Override
    public LogResult call() throws Exception {
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
        return getLogResult(); // Return the result at the end of the call method
    }

    public LogResult getLogResult() {
        return new LogResult(numSuccess, numFailure, logEntries);
    }

    public void doPost(File image, AlbumsProfile profile) {
        long latency = 0;
        long startTimestamp = System.currentTimeMillis();
        // POST latency calculation
        try {
            ImageMetaData postResponse = apiInstance.newAlbum(image, profile); // Call Get
            latency = System.currentTimeMillis() - startTimestamp;
            logEntries.add(startTimestamp + "," + "POST" + "," + latency+ "," + 200);
            numSuccess++;

        } catch (ApiException e) {
            latency = System.currentTimeMillis() - startTimestamp;
            logEntries.add(startTimestamp + "," + "POST"+ "," + latency+ "," + e.getCode());
            System.err.println("Error in POST: " + e.getMessage());
            numFailure++;
        }
    }

    public void doGet(String albumId) {
        long latency = 0;
        long startTimestamp = System.currentTimeMillis();
        // POST latency calculation
        try {
            AlbumInfo getResponse = apiInstance.getAlbumByKey(albumId); // Call Get
            latency = System.currentTimeMillis() - startTimestamp;
            logEntries.add(startTimestamp + "," + "GET"+ "," + latency + "," + 200);
            numSuccess++;

        } catch (ApiException e) {
            latency = System.currentTimeMillis() - startTimestamp;
            logEntries.add(startTimestamp + "," + "GET"+ "," + latency+ "," + e.getCode());
            System.err.println("Error in Get: " +e.getMessage());
            numFailure++;
        }
    }

}
