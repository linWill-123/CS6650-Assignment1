import java.util.concurrent.atomic.AtomicInteger;

public class ClientPart1 {
  public ClientPart1() { }

  public static void main(String[] args) throws InterruptedException {
    int threadGroupSize;
    int numThreadGroups;
    int delaySeconds;

    // Local server path
    String localBaseUrl = "http://localhost:8080/AlbumServlet_war_exploded";
    String localBaseUrlGo = "http://localhost:8080";

    // AWS Instance URl
    String baseUrl = "http://34.221.232.209:8080/AlbumServlet_war";
    String baseUrlGo = "http://34.221.232.209:8080";

    /* Test for Java */
    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
    threadGroupSize = 10;
    numThreadGroups = 10;
    delaySeconds = 2;
    long test1Throughput = RunningThreads.runThreads(baseUrl,threadGroupSize,numThreadGroups,delaySeconds);

    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
    threadGroupSize = 10;
    numThreadGroups = 20;
    delaySeconds = 2;
    long test2Throughput = RunningThreads.runThreads(baseUrl,threadGroupSize,numThreadGroups,delaySeconds);

    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
    threadGroupSize = 10;
    numThreadGroups = 30;
    delaySeconds = 2;
    long test3Throughput = RunningThreads.runThreads(baseUrl,threadGroupSize,numThreadGroups,delaySeconds);

    /*Test for Go*/
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 10;
//    delaySeconds = 2;
//    long test1GoThroughput = RunningThreads.runThreads(baseUrlGo,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 20;
//    delaySeconds = 2;
//    long test2GoThroughput = RunningThreads.runThreads(baseUrlGo,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 30;
//    delaySeconds = 2;
//    long test3GoThroughput = RunningThreads.runThreads(baseUrlGo,threadGroupSize,numThreadGroups,delaySeconds);

    /* Test for Java Locally */
//    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 10;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrl,threadGroupSize,numThreadGroups,delaySeconds);
//
//    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 20;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrl,threadGroupSize,numThreadGroups,delaySeconds);
//
//    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 30;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrl,threadGroupSize,numThreadGroups,delaySeconds);

    /*Test for Go Locally*/
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 10;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrlGo,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 20;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrlGo,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 30;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrlGo,threadGroupSize,numThreadGroups,delaySeconds);

  }
}
