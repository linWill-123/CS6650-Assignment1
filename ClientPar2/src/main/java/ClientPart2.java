public class ClientPart2 {
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

    // file names
    String fileJavaTask1 = "file-java-task1.csv";
    String fileJavaTask2 = "file-java-task2.csv";
    String fileJavaTask3 = "file-java-task3.csv";

    String fileGoTask1 = "file-go-task1.csv";
    String fileGoTask2 = "file-go-task2.csv";
    String fileGoTask3 = "file-go-task3.csv";

    String step6File = "step6.csv";


//    /* Test for Java */
    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
    threadGroupSize = 10;
    numThreadGroups = 10;
    delaySeconds = 2;
    RunningThreads.runThreads(baseUrl,fileJavaTask1,threadGroupSize,numThreadGroups,delaySeconds);

    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
    threadGroupSize = 10;
    numThreadGroups = 20;
    delaySeconds = 2;
    RunningThreads.runThreads(baseUrl,fileJavaTask2,threadGroupSize,numThreadGroups,delaySeconds);

    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
    threadGroupSize = 10;
    numThreadGroups = 30;
    delaySeconds = 2;
    RunningThreads.runThreads(baseUrl,fileJavaTask3,threadGroupSize,numThreadGroups,delaySeconds);
      RunningThreads.step6Calculation(fileJavaTask3,step6File);

//    /*Test for Go*/
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 10;
//    delaySeconds = 2;
//    RunningThreads.runThreads(baseUrlGo,fileGoTask1,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 20;
//    delaySeconds = 2;
//    RunningThreads.runThreads(baseUrlGo,fileGoTask2,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 30;
//    delaySeconds = 2;
//    RunningThreads.runThreads(baseUrlGo,fileGoTask3,threadGroupSize,numThreadGroups,delaySeconds);
//    RunningThreads.step6Calculation(fileGoTask3,step6File);

    /* Test for Java, locally */
//    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 10;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrl,fileJavaTask1,threadGroupSize,numThreadGroups,delaySeconds);
//
//    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 20;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrl,fileJavaTask2,threadGroupSize,numThreadGroups,delaySeconds);
//
//    System.out.println("Java with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 30;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrl,fileJavaTask3,threadGroupSize,numThreadGroups,delaySeconds);
//      RunningThreads.step6Calculation(fileJavaTask3,step6File);

    /*Test for Go, locally*/
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 10, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 10;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrlGo,fileGoTask1,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 20, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 20;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrlGo,fileGoTask2,threadGroupSize,numThreadGroups,delaySeconds);
//    System.out.println("Go with threadGroupSize = 10, numThreadGroups = 30, delaySeconds = 2, result:");
//    threadGroupSize = 10;
//    numThreadGroups = 30;
//    delaySeconds = 2;
//    RunningThreads.runThreads(localBaseUrlGo,fileGoTask3,threadGroupSize,numThreadGroups,delaySeconds);
//    RunningThreads.step6Calculation(fileGoTask3,step6File);
  }

}
