import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class ProducerThread implements Callable<int[]> {
    private int numSuccess;
    private int numFailure;

    private int numIterations;
    private DefaultApi apiInstance;
    private String imagePath;

    private BlockingQueue<String> sharedQueue;

    public ProducerThread(String serverUrl, String imagePath,  BlockingQueue<String> sharedQueue, int numIterations) {
        ApiClient client = new ApiClient();
        client.setBasePath(serverUrl);
        this.apiInstance = new DefaultApi(client);
        this.imagePath = imagePath;
        this.sharedQueue = sharedQueue;
        this.numIterations = numIterations;
    }

    @Override
    public int[] call() {
        File image = new File(imagePath);
        AlbumsProfile profile = new AlbumsProfile();
        profile.setArtist("Eminem");
        profile.setTitle("MMlp2");
        profile.setYear("2001");

        for (int i = 0; i < numIterations; i++) {
            doPost(image,profile);
            doGet("1");
        }

        return new int[] {numSuccess,numFailure};
    }

    public void doPost(File image, AlbumsProfile profile) {
        long latency = 0;
        long startTimestamp = System.currentTimeMillis();
        // POST latency calculation
        try {
            ImageMetaData postResponse = apiInstance.newAlbum(image, profile); // Call Get
            latency = System.currentTimeMillis() - startTimestamp;
            sharedQueue.add(startTimestamp + "," + "POST" + "," + latency+ "," + 200);
            numSuccess++;

        } catch (ApiException e) {
            latency = System.currentTimeMillis() - startTimestamp;
            sharedQueue.add(startTimestamp + "," + "POST"+ "," + latency+ "," + e.getCode());
            System.err.println(e.getMessage());
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
            sharedQueue.add(startTimestamp + "," + "GET"+ "," + latency + "," + 200);
            numSuccess++;

        } catch (ApiException e) {
            latency = System.currentTimeMillis() - startTimestamp;
            sharedQueue.add(startTimestamp + "," + "GET"+ "," + latency+ "," + e.getCode());
            System.err.println(e.getMessage());
            numFailure++;
        }
    }

}