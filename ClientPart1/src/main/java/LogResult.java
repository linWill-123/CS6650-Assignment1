import java.util.List;

public class LogResult {
    private int numSuccess;
    private int numFailure;

    public LogResult(int numSuccess, int numFailure) {
        this.numSuccess = numSuccess;
        this.numFailure = numFailure;
    }

    public int getNumSuccess() {
        return numSuccess;
    }

    public int getNumFailure() {
        return numFailure;
    }

}
