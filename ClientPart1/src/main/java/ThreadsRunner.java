import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class ThreadsRunner {
    public static void runThreads(final String baseUrl, final String imagePath, final String csvFilePath,
                                  int threadGroupSize, int numThreadGroups, int delaySeconds) throws InterruptedException, ExecutionException {

        // Initialization
        Queue<Future<LogResult>> futures = new ConcurrentLinkedQueue<>();

        ExecutorService executorService = Executors.newFixedThreadPool(threadGroupSize);
        CountDownLatch latch = new CountDownLatch(threadGroupSize);

        for (int i = 0; i < threadGroupSize; i++) {
            ProducerThread task = new ProducerThread(baseUrl, imagePath, 100, latch);
            Future<LogResult> future = executorService.submit(task);
            futures.add(future);
        }

        executorService.shutdown();
        latch.await();


        // Start timing
        long testStartTime = System.currentTimeMillis();

        // Submit consumer thread to write post results to csv

        executorService = Executors.newFixedThreadPool(numThreadGroups);
        CountDownLatch groupLatch = new CountDownLatch(numThreadGroups);

        for (int i = 0; i < numThreadGroups; i++) {
            executorService.submit(() -> {
                ExecutorService innerExecutorService = Executors.newFixedThreadPool(threadGroupSize);
                CountDownLatch innerGroupLatch = new CountDownLatch(threadGroupSize);

                for (int j = 0; j < threadGroupSize; j++) {
                    ProducerThread task = new ProducerThread(baseUrl, imagePath, 1000, innerGroupLatch);
                    Future<LogResult> future = innerExecutorService.submit(task);
                    futures.add(future);
                }

                innerExecutorService.shutdown();

                try {
                    innerGroupLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                groupLatch.countDown();

            });

            try {
                Thread.sleep(delaySeconds * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        executorService.shutdown();
        groupLatch.await();

        // end timing
        long testEndTime = System.currentTimeMillis();

        long wallTime = (testEndTime - testStartTime) / 1000;


        long totalSuccess = 0;
        long totalFailure = 0;

        for (Future<LogResult> future : futures) {
            try {
                LogResult result = future.get();
                totalSuccess += result.getNumSuccess();
                totalFailure += result.getNumFailure();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // handle the interrupt
            } catch (ExecutionException e) {
                // handle the exception thrown from the task
            }
        }


        long throughput = totalSuccess / wallTime;

        System.out.println("Wall Time: " + wallTime + " seconds");
        System.out.println("Throughput: " + throughput + " requests/second");
        System.out.println("Successful Requests: " + totalSuccess);
        System.out.println("Failed Requests: " + totalFailure);;


    }

}
