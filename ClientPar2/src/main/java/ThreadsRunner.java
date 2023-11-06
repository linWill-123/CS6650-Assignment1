import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ThreadsRunner {
    public static void runThreads(final String baseUrl, final String csvFilePath, int threadGroupSize,
                                  int numThreadGroups, int delaySeconds) throws InterruptedException, ExecutionException {

        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        String imagePath = "/Users/willxzy/Downloads/MicrosoftTeams-image.png";

        ExecutorService executor = Executors.newFixedThreadPool(threadGroupSize);
        List<Future<int[]>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Future<int[]> executedRes = executor.submit(new ProducerThread(baseUrl,imagePath,queue,100));
            futures.add(executedRes);
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        // Start timing
        long testStartTime = System.currentTimeMillis();

        // Submit consumer thread to write post results to csv
        ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
        Future<?> consumerFuture = consumerExecutor.submit(new ConsumerThread(queue,csvFilePath));

        executor = Executors.newFixedThreadPool(numThreadGroups*threadGroupSize);

        for (int i = 0; i < numThreadGroups; i++) {
            for (int j = 0; j < threadGroupSize; j++) {
                Future<int[]> executedRes = executor.submit(new ProducerThread(baseUrl,imagePath,queue,1000));
                futures.add(executedRes);
            }
            Thread.sleep(delaySeconds * 1000);
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        queue.put("POISON_PILL"); // after all producer threads recorded the logs, add this poison_pill to end of queue

        consumerFuture.get();
        consumerExecutor.shutdown();

        // end timing
        long testEndTime = System.currentTimeMillis();

        long wallTime = (testEndTime - testStartTime) / 1000;

        long totalSuccess = 0;
        long totalFailure = 0;

        for (int i = 0; i < futures.size(); i++) {
            int[] executedRes = futures.get(i).get();
            totalSuccess += executedRes[0];
            totalFailure += executedRes[1];
        }

        long throughput = totalSuccess / wallTime;

        System.out.println("Wall Time: " + wallTime + " seconds");
        System.out.println("Throughput: " + throughput + " requests/second");
        System.out.println("Successful Requests: " + totalSuccess);
        System.out.println("Failed Requests: " + totalFailure);;

    }

    private static void writeBatchToFile(String filename, BlockingQueue<String> queue) {
        List<String> batchToWrite = new ArrayList<>();
        while (!queue.isEmpty()) {
            batchToWrite.add(queue.poll());
        }

        try (FileWriter fw = new FileWriter(filename, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (String log : batchToWrite) {
                out.println(log);
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private void calcMetrics(String csvFilePath) {
        List<Long> latencies = CSVParser.parseLatenciesForSuccessfulRequests(csvFilePath); // Parse latency information from csv
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

    public static void step6Calculation(String srcFile, String dstFile, long wallTime, long testStartTime) {
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
