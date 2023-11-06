import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;

public class ConsumerThread implements Runnable {
    private BlockingQueue<String> queue;
    private String filePath;

    public ConsumerThread(BlockingQueue<String> queue, String filePath) {
        this.queue = queue;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Path file = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE)) {
            while (true) {
                String logEntry = queue.take();
                // Check for a special "poison pill" log entry that indicates no more items will be added to the queue
                if (logEntry.equals("POISON_PILL")) {
                    break;
                }
                writer.write(logEntry);
                writer.newLine();
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
