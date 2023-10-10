import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RunningThreads {
  private static AtomicInteger success = new AtomicInteger(0);
  private static AtomicInteger failure = new AtomicInteger(0);

  private static final int BATCH_SIZE = 1000;
  private static List<String> logBatch = Collections.synchronizedList(new ArrayList<String>());

  private static long wallTime;
  private static long testStartTime;
  private static long testEndTime;

  public RunningThreads() { }

  public static void runThreads(final String basePath, final String filePath, int threadGroupSize,
      int numThreadGroups, int delaySeconds) throws InterruptedException{
    ExecutorService executorService = Executors.newFixedThreadPool(threadGroupSize);

    for (int i = 0; i < 10; i++) {
      submitTask(basePath, filePath, executorService, 100);
    }

    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

    executorService = Executors.newFixedThreadPool(threadGroupSize);
    testStartTime = System.currentTimeMillis();

    for (int group = 0; group < numThreadGroups; group++) {
      for (int i = 0; i < threadGroupSize; i++) {
        submitTask(basePath,filePath, executorService, 1000);
      }
      Thread.sleep(delaySeconds * 1000);
    }

    executorService.shutdown();
    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

    testEndTime = System.currentTimeMillis();
    wallTime = (testEndTime - testStartTime) / 1000;
    long throughput = success.get() / wallTime;

    System.out.println("Wall Time: " + wallTime + " seconds");
    System.out.println("Throughput: " + throughput + " requests/second");
    System.out.println("Successful Requests: " + success);
    System.out.println("Failed Requests: " + failure);

    List<Long> latencies = new ArrayList<>();
    latencies = CSVParser.parseLatenciesForSuccessfulRequests(filePath); // Parse latency information from csv
    Collections.sort(latencies);
    // Mean
    long sum = 0;
    for (long latency : latencies) {
      sum += latency;
    }
    double mean = (double) sum / latencies.size();
    System.out.println("Mean: " + mean);

// Median
    double median;
    int middle = latencies.size() / 2;
    if (latencies.size() % 2 == 0) {
      median = (latencies.get(middle - 1) + latencies.get(middle)) / 2.0;
    } else {
      median = latencies.get(middle);
    }
    System.out.println("Median: " + mean);

// p99 (99th percentile)
    int p99Index = (int) Math.ceil(0.99 * latencies.size()) - 1;
    long p99Value = latencies.get(p99Index);
    System.out.println("p99: " + mean);

// Min and Max
    long min = latencies.get(0);
    long max = latencies.get(latencies.size() - 1);
    System.out.println("Min: " + min);
    System.out.println("Max: " + max);

  }

  private static void submitTask(final String basePath, final String filePath,
      ExecutorService executorService, final int loopCount) {

    executorService.submit(new Runnable() {
      @Override
      public void run() {
        ApiClient client = new ApiClient();
        client.setBasePath(basePath);
        DefaultApi apiInstance = new DefaultApi(client);

        int localSuccess = 0;
        int localFailure = 0;

        File image = new File("/Users/willxzy/Downloads/Assignment1.Problem1.jpg");
        AlbumsProfile profile = new AlbumsProfile();
        profile.setArtist("Eminem");
        profile.setTitle("MMlp2");
        profile.setYear("2001");

        for (int i = 0; i < loopCount; i++) {
          long startTimestamp;
          long endTimestamp;
          long latency;

          // POST latency calculation
          startTimestamp = System.currentTimeMillis();
          try {
            ImageMetaData postResponse = apiInstance.newAlbum(image, profile); // Call Get
            endTimestamp = System.currentTimeMillis();
            latency = endTimestamp - startTimestamp;
            // Write POST to file
            appendToCSV(filePath, startTimestamp, "POST", latency, 200);  // For POST
            // Update Success count
            localSuccess++;

          } catch (ApiException e) {
            endTimestamp = System.currentTimeMillis();
            latency = endTimestamp - startTimestamp;
            appendToCSV(filePath, startTimestamp, "POST", latency, e.getCode());
            System.err.println("Exception when calling DefaultApi");
            // Update Failure Count
            localFailure++;
          }

          // GET latency calculation
          try {
            startTimestamp = System.currentTimeMillis();
            String albumID = "1"; // hardcoded albumID, GET returns pre-existing data, so albumID doesn't matter
            AlbumInfo getResponse = apiInstance.getAlbumByKey(albumID); // Call GET
            endTimestamp = System.currentTimeMillis();
            latency = endTimestamp - startTimestamp;
            // Write GET to file
            appendToCSV(filePath, startTimestamp, "GET", latency, 200);   // For GET

            localSuccess++;
          } catch (ApiException e) {
            endTimestamp = System.currentTimeMillis();
            latency = endTimestamp - startTimestamp;
            appendToCSV(filePath, startTimestamp, "GET", latency, e.getCode());
            System.err.println("Exception when calling DefaultApi");
            // Update Failure Count
            localFailure++;
          }
        }

        success.addAndGet(localSuccess);
        failure.addAndGet(localFailure);
      }
    });
  }

  private synchronized static void appendToCSV(String filename, long startTime, String requestType, long latency, int responseCode) {
    logBatch.add(startTime + "," + requestType + "," + latency + "," + responseCode);

    if (logBatch.size() >= BATCH_SIZE) {
      writeBatchToFile(filename);
      logBatch.clear();
    }
  }

  private synchronized static void writeBatchToFile(String filename) {
    try (FileWriter fw = new FileWriter(filename, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      for (String log : logBatch) {
        out.println(log);
      }
    } catch (IOException e) {
      System.err.println("Error writing to CSV file: " + e.getMessage());
    }
  }

  public static void step6Calculation(String srcFile, String dstFile) {
    // Create array with size of wallTime, where array[i] = ith second
    int[] throughputs = new int[((int) wallTime) + 1];

    // Parse the start times from the csv we wrote to during thread execution
    List<Long> startTimes = CSVParser.parseStartTimes(srcFile);  // Implement this method to get all request start times from CSV

    // Go through each request and add the request to corresponding entry in array
    for (long startTime : startTimes) {
      int bucketIndex = (int) ((startTime - testStartTime) / 1000);  // Convert to seconds
      // filter out invalid errors
      if (bucketIndex >= throughputs.length || bucketIndex < 0) {
        continue;  // Skip adding to the bucket, but continue processing
      }
      throughputs[bucketIndex]++;
    }

    // Write result to target dstFile
    try (FileWriter fw = new FileWriter(dstFile, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      for (int i = 0; i < throughputs.length; i++) {
        out.println(i + "," + throughputs[i]);
      }
    } catch (IOException e) {
      System.err.println("Error writing to throughput CSV file: " + e.getMessage());
    }
  }

}
